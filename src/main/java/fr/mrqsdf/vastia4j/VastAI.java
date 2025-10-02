package fr.mrqsdf.vastia4j;

import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.service.AccountService;
import fr.mrqsdf.vastia4j.service.OfferService;

public class VastAI {

    private final VastAIClient client;
    private final AccountService accountService;
    private final OfferService offerService;

    public VastAI(String apiKey) { this(new VastAIClient(apiKey)); }
    public VastAI(String apiKey, String baseUrl) { this(new VastAIClient(apiKey, baseUrl)); }

    public VastAI(VastAIClient client) {
        this.client = client;
        this.accountService = new AccountService(client);
        this.offerService = new OfferService(client);
    }

    public VastAIClient getClient() { return client; }
    public AccountService account() { return accountService; }
    public OfferService offers() { return offerService; }
}
