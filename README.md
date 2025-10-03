# VastIA4J

Java library to consume the [Vast.ai](https://vast.ai/) REST API with an idiomatic API based on `java.net.http` and Gson.

## Table of Contents
- [Overview](#overview)
- [Current Features](#current-features)
- [Installation](#installation)
  - [Using Gradle](#using-gradle)
  - [Using Maven](#using-maven)
- [Usage Guide](#usage-guide)
  - [Client Initialization](#client-initialization)
  - [Account Management](#account-management)
  - [GPU Offer Search](#gpu-offer-search)
  - [Instance Management](#instance-management)
  - [Accessing Templates](#accessing-templates)
  - [Exposed Data Models](#exposed-data-models)
- [Changelog](#changelog)
- [Future Improvements](#future-improvements)
- [Full Main Example](#full-main-example)

## Overview
VastIA4J provides a central HTTP client (`VastAIClient`) wrapped in a high-level façade (`VastAI`). This façade instantiates several specialized services (account, offers, instances, templates) and exposes convenient constructors to configure the API key and base URL.

## Current Features
- HTTP client based on `HttpClient` with Gson serialization/deserialization and unified error handling via `VastAIException`.
- Account service to retrieve the current user and balance (`AccountService`).
- Offer service to query GPU offers through the legacy (`POST /bundles/`) and modern (`PUT /search/asks/`) endpoints using the `OfferQuery` builder.
- Instance service to create, start, stop, restart, label, destroy, or list instances, as well as fetch extended details (`InstanceService`).
- Template service (deprecated on the API side) kept for compatibility with `TemplateSearchQuery` filters.

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
Replace `1.0.0` with the distributed `.jar` version.

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
Update the version and path to match your local setup. Remember to copy the `.jar` into `libs/` and only enable the `systemPath` option if Maven distribution is not desired.

## Usage Guide
### Client Initialization
```java
import fr.mrqsdf.vastia4j.VastAI;

VastAI vast = new VastAI("YOUR_API_KEY");
// or
VastAI vastCustom = new VastAI("YOUR_API_KEY", "https://console.vast.ai/api/v0");
```
The constructor also accepts a preconfigured `VastAIClient` (proxy, custom timeouts, etc.).

### Account Management
```java
import fr.mrqsdf.vastia4j.model.AccountBalance;
import fr.mrqsdf.vastia4j.model.CurrentUser;

AccountBalance balance = vast.account().getBalance();
System.out.println("Available credit: " + balance.credit());

CurrentUser currentUser = vast.account().getClient();
System.out.println("User: " + currentUser.username());
```
The service automatically fetches `GET /users/current/` and exposes the balance and credits.

### GPU Offer Search
```java
import fr.mrqsdf.vastia4j.query.OfferQuery;
import fr.mrqsdf.vastia4j.query.OfferField;
import fr.mrqsdf.vastia4j.query.Op;
import fr.mrqsdf.vastia4j.query.Direction;

OfferQuery query = new OfferQuery()
        .where(OfferField.GPU_NAME, Op.EQ, "RTX 4090")
        .where("dlperf", Op.GT, 2_000)
        .orderBy(OfferField.PRICE_PER_HOUR_USD, Direction.ASC)
        .limit(20);

var offers = vast.offers().searchNew(query);
offers.forEach(offer -> System.out.printf("%s @ %s$/h\n", offer.gpuName(), offer.pricePerHourUSD()));
```
The `OfferService` automatically handles the JSON structure (`select_cols`, default filters, order) and adapts to both endpoints (`search()` legacy, `searchNew()` modern).

### Instance Management
```java
import fr.mrqsdf.vastia4j.model.instance.CreateInstanceRequest;

CreateInstanceRequest req = new CreateInstanceRequest()
        .templateId(12345L)
        .disk(50.0)
        .targetState("running")
        .label("exp-train-01");

var created = vast.instances().createInstance(offerId, req);
System.out.println("Instance created: " + created.instanceId());

vast.instances().start(created.instanceId());
var running = vast.instances().listByState("running");
var details = vast.instances().show(created.instanceId());
vast.instances().stop(created.instanceId());
vast.instances().destroy(created.instanceId());
```
`InstanceService` enforces validations (minimum disk size, Docker image with tag) and maps the endpoints `PUT /asks/{id}/`, `PUT /instances/{id}/`, `GET /instances/{id}/`, etc.

### Accessing Templates
Although the Vast.ai template API is currently unstable, `TemplateService` remains available:
```java
var myTemplates = vast.templates().searchMyTemplates("diffusion", "created_at");
```
Use `TemplateSearchQuery` to combine filters (`select_filters`), text search, and sorting. The method is annotated `@Deprecated` on the façade to reflect the remote API status.

### Exposed Data Models
- `Offer`: all key attributes of GPU offers (price, performance, network, reliability...).
- `AccountBalance` & `CurrentUser`: account information and credits.
- `CreateInstanceRequest` / `CreateInstanceResponse`: instance provisioning (volume support, environment variables, SSH/Jupyter options).
- `InstanceSummary`, `InstanceDetails`: metadata, current state, SSH access.
- `Template` & `TemplateSearchResponse`: description of shared and personal templates.

## Changelog
- Generic HTTP client with custom Gson configuration (`ApiRightsAdapter`, `EndpointMethodsAdapter` adapters).
- High-level services: `AccountService`, `OfferService`, `TemplateService`, `InstanceService`.
- Fluent builders: `OfferQuery`, `TemplateSearchQuery`, and the enums `OrderField`, `OfferField`, `Direction`, `Op`.
- Instance models covering creation, listing, and details (`CreateInstanceRequest`, `InstanceSummary`, `InstanceDetails`).

## Future Improvements
- Publish the library on a centralized Maven repository (Maven Central, GitHub Packages) to simplify integration.
- Cover more Vast.ai endpoints: volume management, detailed billing, webhooks.
- Add integration tests with a stub server or HTTP snapshots to secure future changes.
- Provide a Kotlin/Coroutine or Reactor module for asynchronous consumption.
- Generate Javadoc documentation published automatically (GitHub Pages).

## Full Main Example
```java
package fr.mrqsdf.vastia4j;

import fr.mrqsdf.vastia4j.model.AccountBalance;
import fr.mrqsdf.vastia4j.model.Offer;
import fr.mrqsdf.vastia4j.model.Template;
import fr.mrqsdf.vastia4j.model.instance.*;
import fr.mrqsdf.vastia4j.query.*;

import java.util.*;

/**
 * Small demonstration of the Vast.ai Java client.
 */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please provide your Vast.ai API key as the first argument.");
            return;
        }

        VastAI vastAI = new VastAI(args[0]);

        // 1) Balance
        AccountBalance balance = vastAI.account().getBalance();
        System.out.println("Balance: " + balance.balance() + " (credit: " + balance.credit() + ")");
        long userId = vastAI.account().getClient().getId();

        // 2) Offer search (sorted by score descending)
        OfferQuery q = new OfferQuery()
                .where(OfferField.GPU_NAME, Op.IN, Set.of("RTX 3060"))
                .where(OfferField.STATIC_IP, Op.EQ, false)
                .orderBy(OrderField.SCORE, Direction.DESC)
                .limit(40);

        List<Offer> offers = vastAI.offers().search(q);      // POST /bundles/
        //offers = vastAI.offers().searchNew(q); // PUT /search/asks/

        offers.sort(Comparator.comparing(Offer::dphTotal));

        System.out.println("\nTop offers:");
        for (Offer o : offers) {
            System.out.printf(
                    "- offer #%d | %s x%d | $/h=%.4f | dlperf=%.1f | rel=%.3f | geo=%s | rentable=%s%n",
                    o.id(), o.gpuName(), o.numGpus(), nz(o.dphTotal()),
                    nz(o.dlperf()), nz(o.reliability()), o.geoCountryCode(),
                    o.rentable()
            );
        }

        /*List<Template> publicTemplates = vastAI.templates()
                .searchAll("pytorch", "created_at");
        System.out.println("\nPublic templates (pytorch):");
        for (Template t : publicTemplates) {
            System.out.printf("- #%d %s | image=%s | public=%s%n",
                    t.id(), t.name(), t.image(), t.isPublic());
        };

// 4) Personal templates (server-side filter) sorted by creation date
        var myTemplates = vastAI.templates().searchMyTemplates(null, "created_at");
        System.out.println("\nMy templates:");
        for (var t : myTemplates) {
            System.out.printf("- #%d %s | hash=%s%n", t.id(), t.name(), t.hashId());
        }


        VastAI alt = new VastAI(args[0], "https://cloud.vast.ai/api/v0");
        List<Template> t = alt.templates().searchAll(null, null);
        System.out.println("templates via cloud.vast.ai = " + t.size());*/

        long offerId = offers.get(0).id();
        long templateId = -1; // Replace with a template id if you want to rely on an existing template.

        CreateInstanceRequest req = new CreateInstanceRequest()
                .templateId(null)  // Set to null when providing a custom image instead.
                .templateHashId("a910281dde7abd591fe1c8a7ee9312d6") // Demo hashId: replace with a valid value for real usage.
                //.image("nvidia/cuda:11.6.2-cudnn8-runtime-ubuntu20.04") // Uncomment to rely on a public image.
                .disk(35.0)                                             // Minimum is 8 GB.
                .runtypeEnum(RunType.SSH)                                // Alternatively use JUPYTER.
                .targetState("running")
                .label("java-sdk-demo");                 // Or "stopped" if you plan to start later.

        CreateInstanceResponse created = vastAI.instances().createInstance(offerId, req);
        System.out.println("Create success=" + created.success() + " new_contract=" + created.newContract());

        // Instance identifiers usually match the "new_contract" value.
        long instanceId = created.newContract();

        // 2) Retrieve details
        InstanceDetails details = vastAI.instances().show(instanceId);
        System.out.println("Instance #" + details.instances().id()
                + " state=" + details.instances().curState()
                + " ssh=" + details.instances().sshHost() + ":" + details.instances().sshPort());

        // 3) Stop
        //vastAI.instances().stop(instanceId);
        System.out.println("Stopped instance " + instanceId);

        // 4) Start again
        //vastAI.instances().start(instanceId);
        System.out.println("Started instance " + instanceId);

        // 5) Reboot (internal stop/start)
        //vastAI.instances().reboot(instanceId);
        System.out.println("Rebooted instance " + instanceId);


        // 1) List every instance
        List<InstanceSummary> all = vastAI.instances().list();
        System.out.println("=== Instances ===");
        if (all.isEmpty()) {
            System.out.println("(no instances)");
        } else {
            for (InstanceSummary s : all) {
                System.out.printf("#%d | %s | %s | GPU=%s x%d | $/h=%.4f%n",
                        s.id(),
                        s.curState(),
                        s.label(),
                        s.gpuName(),
                        s.numGpus() == null ? 0 : s.numGpus(),
                        s.dphTotal() == null ? Double.NaN : s.dphTotal()
                );
            }

        }
        // 6) Destroy
        vastAI.instances().destroy(instanceId);
        System.out.println("Destroyed instance " + instanceId);
    }

    private static double nz(Double d) {
        return d == null ? Double.NaN : d;
    }
}
```
