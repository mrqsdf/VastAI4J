package fr.mrqsdf.vastia4j;

import fr.mrqsdf.vastia4j.model.AccountBalance;
import fr.mrqsdf.vastia4j.model.Offer;
import fr.mrqsdf.vastia4j.model.Template;
import fr.mrqsdf.vastia4j.query.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/** Small demonstration of the Vast.ai Java client. */
public final class Main {
    private Main() {}

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
    }

    private static double nz(Double d) { return d == null ? Double.NaN : d; }
}
