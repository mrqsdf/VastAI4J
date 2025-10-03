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

        // 2) Recherche d’offres :
        // - tri par score (desc)
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

// 4) Mes templates uniquement (filtre serveur) + tri par date de création
        var myTemplates = vastAI.templates().searchMyTemplates(null, "created_at");
        System.out.println("\nMy templates:");
        for (var t : myTemplates) {
            System.out.printf("- #%d %s | hash=%s%n", t.id(), t.name(), t.hashId());
        }


        VastAI alt = new VastAI(args[0], "https://cloud.vast.ai/api/v0");
        List<Template> t = alt.templates().searchAll(null, null);
        System.out.println("templates via cloud.vast.ai = " + t.size());*/

        long offerId = offers.get(0).id();
        long templateId = -1; // mettre un id de template si besoin, sinon null

        CreateInstanceRequest req = new CreateInstanceRequest()
                .templateId(null)  // <- tu peux mettre null si tu utilises "image"
                .templateHashId("a910281dde7abd591fe1c8a7ee9312d6") // <- ou le hashId (this is demo ID, you can't use it, is for demo only)
                //.image("nvidia/cuda:11.6.2-cudnn8-runtime-ubuntu20.04") // si pas de template
                .disk(35.0)                                             // >= 8
                .runtypeEnum(RunType.SSH)                                // ou JUPYTER
                .targetState("running")
                .label("java-sdk-demo");                 // ou "stopped"

        CreateInstanceResponse created = vastAI.instances().createInstance(offerId, req);
        System.out.println("Create success=" + created.success() + " new_contract=" + created.newContract());

        // NB: l'id d’instance (= id du contrat) est généralement "new_contract"
        long instanceId = created.newContract();

        // 2) Afficher les infos
        InstanceDetails details = vastAI.instances().show(instanceId);
        System.out.println("Instance #" + details.instances().id()
                + " state=" + details.instances().curState()
                + " ssh=" + details.instances().sshHost() + ":" + details.instances().sshPort());

        // 3) Stopper
        //vastAI.instances().stop(instanceId);
        System.out.println("Stopped instance " + instanceId);

        // 4) Redémarrer (start)
        //vastAI.instances().start(instanceId);
        System.out.println("Started instance " + instanceId);

        // 5) Reboot (stop/start interne)
        //vastAI.instances().reboot(instanceId);
        System.out.println("Rebooted instance " + instanceId);


        // 1) Lister toutes les instances
        List<InstanceSummary> all = vastAI.instances().list();
        System.out.println("=== Instances ===");
        if (all.isEmpty()) {
            System.out.println("(aucune instance)");
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
        // 6) Détruire
        vastAI.instances().destroy(instanceId);
        System.out.println("Destroyed instance " + instanceId);
    }

    private static double nz(Double d) {
        return d == null ? Double.NaN : d;
    }
}
