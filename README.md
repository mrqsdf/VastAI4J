# VastIA4J

Bibliothèque Java pour consommer l'API REST de [Vast.ai](https://vast.ai/) avec une API idiomatique basée sur `java.net.http` et Gson.

## Sommaire
- [Aperçu](#aperçu)
- [Fonctionnalités actuelles](#fonctionnalités-actuelles)
- [Installation](#installation)
  - [Utilisation avec Gradle](#utilisation-avec-gradle)
  - [Utilisation avec Maven](#utilisation-avec-maven)
- [Guide d'utilisation](#guide-dutilisation)
  - [Initialisation du client](#initialisation-du-client)
  - [Gestion du compte](#gestion-du-compte)
  - [Recherche d'offres GPU](#recherche-doffres-gpu)
  - [Gestion des instances](#gestion-des-instances)
  - [Accès aux modèles (templates)](#accès-aux-modèles-templates)
  - [Modèles de données exposés](#modèles-de-données-exposés)
- [Journal des ajouts](#journal-des-ajouts)
- [Axes d'amélioration futurs](#axes-damélioration-futurs)

## Aperçu
VastIA4J fournit un client HTTP central (`VastAIClient`) encapsulé dans une façade haut niveau (`VastAI`). Cette façade instancie plusieurs services spécialisés (compte, offres, instances, templates) et expose des constructeurs pratiques pour configurer la clé API et l'URL de base.

## Fonctionnalités actuelles
- Client HTTP basé sur `HttpClient` avec sérialisation/désérialisation Gson et gestion d'erreur unifiée via `VastAIException`.
- Service compte pour récupérer l'utilisateur courant et le solde (`AccountService`).
- Service offres pour interroger les offres GPU via les endpoints historiques (`POST /bundles/`) et modernes (`PUT /search/asks/`) en utilisant le builder `OfferQuery`.
- Service instances pour créer, démarrer, arrêter, redémarrer, étiqueter, détruire ou lister les instances, ainsi que récupérer les détails étendus (`InstanceService`).
- Service templates (déprécié côté API) conservé pour compatibilité avec les filtres `TemplateSearchQuery`.

## Installation
### Utilisation avec Gradle
1. Copiez le `.jar` dans un répertoire `libs/` de votre projet.
2. Ajoutez un dépôt `flatDir` et référencez le fichier :

```gradle
repositories {
    mavenLocal()
    flatDir { dirs "libs" }
}

dependencies {
    implementation files("libs/vastia4j-<version>.jar")
}
```

> 💡 Alternative : installez le `.jar` dans votre Maven local (`mvn install:install-file ...`) puis consommez-le via `mavenLocal()`.

### Utilisation avec Maven
Vous pouvez soit référencer directement le fichier, soit l'installer dans votre dépôt local.

**Option 1 — Installation dans le dépôt Maven local :**
```bash
mvn install:install-file \
  -Dfile=/chemin/vers/vastia4j-<version>.jar \
  -DgroupId=fr.mrqsdf \
  -DartifactId=vastia4j \
  -Dversion=<version> \
  -Dpackaging=jar
```
Puis déclarez la dépendance :
```xml
<dependency>
    <groupId>fr.mrqsdf</groupId>
    <artifactId>vastia4j</artifactId>
    <version>1.0.0</version>
</dependency>
```
Remplacez `1.0.0` par la version du `.jar` distribué.

**Option 2 — Référence directe au fichier :**
```xml
<dependency>
    <groupId>fr.mrqsdf</groupId>
    <artifactId>vastia4j</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/libs/vastia4j-<version>.jar</systemPath>
</dependency>
```
Mettez à jour la version et le chemin pour refléter votre installation locale.
Pensez à copier le `.jar` dans `libs/` et à activer l'option `systemPath` uniquement si la distribution Maven n'est pas souhaitable.

## Guide d'utilisation
### Initialisation du client
```java
import fr.mrqsdf.vastia4j.VastAI;

VastAI vast = new VastAI("VOTRE_CLE_API");
// ou
VastAI vastCustom = new VastAI("VOTRE_CLE_API", "https://console.vast.ai/api/v0");
```
Le constructeur accepte aussi un `VastAIClient` préconfiguré (proxy, timeouts personnalisés, etc.).

### Gestion du compte
```java
import fr.mrqsdf.vastia4j.model.AccountBalance;
import fr.mrqsdf.vastia4j.model.CurrentUser;

AccountBalance balance = vast.account().getBalance();
System.out.println("Crédit disponible : " + balance.credit());

CurrentUser currentUser = vast.account().getClient();
System.out.println("Utilisateur : " + currentUser.username());
```
Le service récupère automatiquement `GET /users/current/` et met à disposition le solde et les crédits.

### Recherche d'offres GPU
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
Le service `OfferService` gère automatiquement la structure JSON (`select_cols`, filtres par défaut, ordre) et s'adapte aux deux endpoints (`search()` historique, `searchNew()` moderne).

### Gestion des instances
```java
import fr.mrqsdf.vastia4j.model.instance.CreateInstanceRequest;

CreateInstanceRequest req = new CreateInstanceRequest()
        .templateId(12345L)
        .disk(50.0)
        .targetState("running")
        .label("exp-train-01");

var created = vast.instances().createInstance(offerId, req);
System.out.println("Instance créée : " + created.instanceId());

vast.instances().start(created.instanceId());
var running = vast.instances().listByState("running");
var details = vast.instances().show(created.instanceId());
vast.instances().stop(created.instanceId());
vast.instances().destroy(created.instanceId());
```
`InstanceService` applique des validations (taille de disque minimale, image Docker avec tag) et mappe les endpoints `PUT /asks/{id}/`, `PUT /instances/{id}/`, `GET /instances/{id}/`, etc.

### Accès aux modèles (templates)
Bien que l'API template Vast.ai soit actuellement instable, `TemplateService` reste disponible :
```java
var myTemplates = vast.templates().searchMyTemplates("diffusion", "created_at");
```
Utilisez `TemplateSearchQuery` pour combiner filtres (`select_filters`), recherche textuelle et tri. La méthode est annotée `@Deprecated` côté façade afin de refléter l'état de l'API distante.

### Modèles de données exposés
- `Offer` : tous les attributs clés des offres GPU (prix, performances, réseau, fiabilité...).
- `AccountBalance` & `CurrentUser` : informations de compte et crédits.
- `CreateInstanceRequest` / `CreateInstanceResponse` : provisionnement d'instances (support volumes, variables d'environnement, options SSH/Jupyter).
- `InstanceSummary`, `InstanceDetails` : métadonnées, état courant, accès SSH.
- `Template` & `TemplateSearchResponse` : description des templates partagés et personnels.

## Journal des ajouts
- Client HTTP générique avec configuration Gson personnalisée (adapters `ApiRightsAdapter`, `EndpointMethodsAdapter`).
- Services haut niveau : `AccountService`, `OfferService`, `TemplateService`, `InstanceService`.
- Builders fluents : `OfferQuery`, `TemplateSearchQuery`, ainsi que les enums `OrderField`, `OfferField`, `Direction`, `Op`.
- Modèles d'instances couvrant la création, la liste et le détail (`CreateInstanceRequest`, `InstanceSummary`, `InstanceDetails`).

## Axes d'amélioration futurs
- Publier la bibliothèque sur un dépôt Maven centralisé (Maven Central, GitHub Packages) pour simplifier l'intégration.
- Couvrir davantage d'endpoints Vast.ai : gestion des volumes, facturation détaillée, webhooks.
- Ajouter des tests d'intégration avec un serveur stub ou des snapshots HTTP pour fiabiliser l'évolution.
- Fournir un module Kotlin/Coroutine ou Reactor pour la consommation asynchrone.
- Générer une documentation Javadoc publiée automatiquement (GitHub Pages).
