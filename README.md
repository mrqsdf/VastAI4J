# VastIA4J

Java library to consume the [Vast.ai](https://vast.ai/) REST API with an idiomatic API based on `java.net.http` and Gson.

## Table of Contents

* [Overview](#overview)
* [Current Features](#current-features)
* [Installation](#installation)

  * [Using Gradle](#using-gradle)
  * [Using Maven](#using-maven)
* [Usage Guide](#usage-guide)

  * [Client Initialization](#client-initialization)
  * [Account Management](#account-management)
  * [GPU Offer Search](#gpu-offer-search)
  * [Instance Management](#instance-management)
  * [Event System](#event-system-spigot-style)
  * [Accessing Templates](#accessing-templates)
  * [Exposed Data Models](#exposed-data-models)
* [Changelog](#changelog)
* [Future Improvements](#future-improvements)
* [Full Main Example](#full-main-example)

## Overview

VastIA4J provides a central HTTP client (`VastAIClient`) wrapped in a high-level façade (`VastAI`). This façade instantiates several specialized services (account, offers, instances, templates) and exposes convenient constructors to configure the API key and base URL.

## Current Features

* HTTP client based on `HttpClient` with Gson serialization/deserialization and unified error handling via `VastAIException`.
* Account service to retrieve the current user and balance (`AccountService`).
* Offer service to query GPU offers through the legacy (`POST /bundles/`) and modern (`PUT /search/asks/`) endpoints using the `OfferQuery` builder.
* Instance service to create, start, stop, restart, label, destroy, or list instances, as well as fetch extended details (`InstanceService`).
* **Event bus & monitor (Spigot-style)**: listen to instance state changes, SSH availability, and port-mapping availability.
* Template service (deprecated on the API side) kept for compatibility with `TemplateSearchQuery` filters.

## Installation

### Using Gradle

1. Copy the `.jar` into a `libs/` directory of your project.
2. Add a `flatDir` repository and reference the file:

```gradle
repositories {
    mavenLocal()
    flatDir { dirs "libs" }
}

dependencies {
    implementation files("libs/vastia4j-<version>.jar")
}
```

> 💡 Alternative: install the `.jar` into your local Maven repository (`mvn install:install-file ...`) and consume it via `mavenLocal()`.

### Using Maven

You can either reference the file directly or install it into your local repository.

**Option 1 — Install into the local Maven repository:**

```bash
mvn install:install-file \
  -Dfile=/path/to/vastia4j-<version>.jar \
  -DgroupId=fr.mrqsdf \
  -DartifactId=vastia4j \
  -Dversion=<version> \
  -Dpackaging=jar
```

Then declare the dependency:

```xml
<dependency>
  <groupId>fr.mrqsdf</groupId>
  <artifactId>vastia4j</artifactId>
  <version>1.0.0</version>
</dependency>
```

**Option 2 — Direct file reference:**

```xml
<dependency>
  <groupId>fr.mrqsdf</groupId>
  <artifactId>vastia4j</artifactId>
  <version>1.0.0</version>
  <scope>system</scope>
  <systemPath>${project.basedir}/libs/vastia4j-<version>.jar</systemPath>
</dependency>
```

## Usage Guide

### Client Initialization

```java
import fr.mrqsdf.vastia4j.VastAI;

VastAI vast = new VastAI("YOUR_API_KEY");
// or
VastAI vastCustom = new VastAI("YOUR_API_KEY", "https://console.vast.ai/api/v0");
```

### Account Management

```java
import fr.mrqsdf.vastia4j.model.AccountBalance;
import fr.mrqsdf.vastia4j.model.CurrentUser;

AccountBalance balance = vast.account().getBalance();
System.out.println("Available credit: " + balance.credit());

CurrentUser currentUser = vast.account().getClient();
System.out.println("User: " + currentUser.getUsername());
```

### GPU Offer Search

```java
import fr.mrqsdf.vastia4j.query.*;

OfferQuery query = new OfferQuery()
        .where(OfferField.GPU_NAME, Op.EQ, "RTX 4090")
        .where("dlperf", Op.GT, 2000)
        .orderBy(OfferField.PRICE_PER_HOUR_USD, Direction.ASC)
        .limit(20);

var offers = vast.offers().searchNew(query);
offers.forEach(o -> System.out.printf("%s @ %s$/h%n", o.gpuName(), o.pricePerHourUSD()));
```

### Instance Management

```java
import fr.mrqsdf.vastia4j.model.instance.CreateInstanceRequest;
import fr.mrqsdf.vastia4j.model.instance.RunType;

CreateInstanceRequest req = new CreateInstanceRequest()
        .templateHashId("your-template-hash")
        .disk(35.0)
        .runtypeEnum(RunType.SSH)
        .targetState("running")
        .label("exp-train-01");

var created = vast.instances().createInstance(offerId, req);
System.out.println("Created instance id: " + created.newContract());

var details = vast.instances().show(created.newContract());
System.out.println("SSH: " + details.instances().sshHost() + ":" + details.instances().sshPort());
```

### Event System (Spigot-style)

VastIA4J ships an **EventBus** and an **InstanceMonitor** that polls the Vast API and fires events when an instance:

* changes state (`InstanceStateChangeEvent`),
* becomes reachable via SSH (`InstanceSshReadyEvent`),
* exposes a Docker-like port mapping (`InstancePortsMappedEvent`).

**Priorities** match Spigot’s order: `LOWEST → LOW → NORMAL → HIGH → HIGHEST → MONITOR`. Use `MONITOR` for read-only/logging handlers.

#### 1) Create a listener

```java
import fr.mrqsdf.vastia4j.event.*;
import fr.mrqsdf.vastia4j.event.instance.*;

public final class MyInstanceListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onState(InstanceStateChangeEvent e) {
        System.out.printf("[STATE] #%d %s -> %s | actual %s -> %s%n",
                e.instanceId(), e.previousCurState(), e.newCurState(),
                e.previousActualStatus(), e.newActualStatus());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSsh(InstanceSshReadyEvent e) {
        System.out.printf("[SSH] #%d %s:%d ready%n",
                e.instanceId(), e.sshHost(), e.sshPort());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPorts(InstancePortsMappedEvent e) {
        System.out.printf("[PORTS] #%d ip=%s map=%s%n",
                e.instanceId(), e.publicIpOrHost(), e.mapping());
    }
}
```

#### 2) Register the listener and start the monitor

```java
import fr.mrqsdf.vastia4j.VastAI;
import fr.mrqsdf.vastia4j.event.EventBus;
import fr.mrqsdf.vastia4j.monitor.InstanceMonitor;

import java.util.concurrent.TimeUnit;

public final class EventsQuickStart {
    public static void main(String[] args) throws Exception {
        VastAI vast = new VastAI("YOUR_API_KEY");

        EventBus bus = new EventBus();
        bus.register(new MyInstanceListener());

        try (InstanceMonitor monitor = new InstanceMonitor(vast, bus)) {
            long instanceId = 123456L;
            // poll every 2 seconds
            monitor.watch(instanceId, 2, TimeUnit.SECONDS);

            // demo: run for 60s
            Thread.sleep(60_000);
        }
    }
}
```

#### 3) Tips

* Use `MONITOR` for logging/analytics.
* To watch multiple instances, call `monitor.watch(id, …)` for each id. Stop with `monitor.stop(id)`.
* `InstanceMonitor` implements `AutoCloseable` — use try-with-resources.

### Accessing Templates

Although the Vast.ai template API is currently unstable, `TemplateService` remains available:

```java
var myTemplates = vast.templates().searchMyTemplates("diffusion", "created_at");
```

### Exposed Data Models

* `Offer`, `OfferListResponse`
* `AccountBalance`, `CurrentUser`
* `CreateInstanceRequest` / `CreateInstanceResponse`
* `InstanceSummary`, `InstanceDetails`
* `Template`, `TemplateSearchResponse`

## Changelog

* Added **EventBus** + **InstanceMonitor** (Spigot-style listeners with priorities).
* Extended instance models and services (create/list/show/manage).
* Offer query builder (`OfferQuery`, `OfferField`, `Direction`, `Op`).

## Future Improvements

* Async dispatcher (thread-pool) & per-event executor.
* `watchAll()` helper to auto-discover instances from `/instances/`.
* Webhooks support if/when provided by Vast.ai.

## Full Main Example

See `src/main/java/.../Main.java` for a complete end-to-end sample (balance, offers, instance creation, details, and lifecycle).
