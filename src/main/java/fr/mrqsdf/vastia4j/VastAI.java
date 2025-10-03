package fr.mrqsdf.vastia4j;

import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.event.EventBus;
import fr.mrqsdf.vastia4j.service.AccountService;
import fr.mrqsdf.vastia4j.service.OfferService;
import fr.mrqsdf.vastia4j.service.InstanceService;
import fr.mrqsdf.vastia4j.service.TemplateService;

/**
 * Main entry point for interacting with the Vast.ai API.
 * Provides access to various services such as account management, offers, templates, and instances.
 */
public class VastAI {

    private final VastAIClient client;
    private final AccountService accountService;
    private final OfferService offerService;
    private final TemplateService templateService;
    private final InstanceService instanceService;

    private final EventBus eventBus;

    /**
     * Constructs a VastAI instance with the provided API key.
     * @param apiKey the API key for authenticating requests
     */
    public VastAI(String apiKey) {
        this(new VastAIClient(apiKey));
    }

    /**
     * Constructs a VastAI instance with the provided API key and base URL.
     * @param apiKey the API key for authenticating requests
     * @param baseUrl the base URL of the Vast.ai API
     */
    public VastAI(String apiKey, String baseUrl) {
        this(new VastAIClient(apiKey, baseUrl));
    }

    /**
     * Constructs a VastAI instance with the provided VastAIClient.
     * @param client the VastAIClient to use for making requests
     */
    public VastAI(VastAIClient client) {
        this.client = client;
        this.accountService = new AccountService(client);
        this.offerService = new OfferService(client);
        this.templateService = new TemplateService(client);
        this.instanceService = new InstanceService(client);
        this.eventBus = new EventBus();
    }

    /**
     * Retrieves the underlying VastAIClient.
     * @return the VastAIClient instance
     */
    public VastAIClient getClient() {
        return client;
    }

    /**
     * Provides access to account-related services.
     * @return the AccountService instance
     */
    public AccountService account() {
        return accountService;
    }

    /**
     * Provides access to offer-related services.
     * @return the OfferService instance
     */
    public OfferService offers() {
        return offerService;
    }

    /**
     * Provides access to template-related services.
     * Note: This service is deprecated as the Vast backend is not working.
     * @return the TemplateService instance
     */
    @Deprecated(since = "Vast Backend not Working")
    public TemplateService templates() {
        return templateService;
    }

    /**
     * Provides access to instance-related services.
     * @return the InstanceService instance
     */
    public InstanceService instances() {
        return instanceService;
    }

    /**
     * Provides access to the event bus for subscribing to and publishing events.
     * @return the EventBus instance
     */
    public EventBus eventBus() {
        return eventBus;
    }
}
