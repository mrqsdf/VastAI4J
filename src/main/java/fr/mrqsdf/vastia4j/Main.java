package fr.mrqsdf.vastia4j;

import fr.mrqsdf.vastia4j.model.AccountBalance;
import fr.mrqsdf.vastia4j.model.OfferListResponse;
import fr.mrqsdf.vastia4j.query.OfferQuery;

/**
 * Small demonstration of the Vast.ai Java client.
 */
public final class Main {

    // Main de test

    private Main() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please provide your Vast.ai API key as the first argument.");
            return;
        }

        VastAI vastAI = new VastAI(args[0]);
        AccountBalance balance = vastAI.account().getBalance();
        System.out.println("Balance: " + balance.getBalance() + " (credit: " + balance.getCredit() + ")");
        OfferListResponse response = vastAI.offers().listBestOffers(OfferQuery.create().verified(true));
        System.out.println("Found " + response.getCount() + " offers");
    }
}
