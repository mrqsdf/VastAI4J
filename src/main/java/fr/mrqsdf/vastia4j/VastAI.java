package fr.mrqsdf.vastia4j;

import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.service.AccountService;
import fr.mrqsdf.vastia4j.service.InstancesService;
import fr.mrqsdf.vastia4j.service.OffersService;

/**
 * Entry point aggregating the Vast.ai Java client and services.
 */
public class VastAI {

    private final VastAIClient client;
    private final OffersService offersService;
    private final InstancesService instancesService;
    private final AccountService accountService;

    public VastAI(String apiKey) {
        this(new VastAIClient(apiKey));
    }

    public VastAI(String apiKey, String baseUrl) {
        this(new VastAIClient(apiKey, baseUrl));
    }

    public VastAI(VastAIClient client) {
        this.client = client;
        this.offersService = new OffersService(client);
        this.instancesService = new InstancesService(client);
        this.accountService = new AccountService(client);
    }

    public VastAIClient getClient() {
        return client;
    }

    public OffersService offers() {
        return offersService;
    }

    public InstancesService instances() {
        return instancesService;
    }

    public AccountService account() {
        return accountService;
    }
}
