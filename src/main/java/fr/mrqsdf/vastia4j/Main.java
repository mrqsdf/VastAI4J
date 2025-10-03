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
