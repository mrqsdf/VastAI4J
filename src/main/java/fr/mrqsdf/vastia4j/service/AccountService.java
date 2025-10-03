package fr.mrqsdf.vastia4j.service;

import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.http.VastAIRequest;
import fr.mrqsdf.vastia4j.model.AccountBalance;
import fr.mrqsdf.vastia4j.model.CurrentUser;

import java.util.Objects;

/**
 * Account oriented helper for Vast.ai endpoints.
 */
public class AccountService implements Service {

    private final VastAIClient client;

    private CurrentUser currentUser;

    /**
     * Creates a new AccountService instance.
     * @param client the VastAIClient to use for requests
     */
    public AccountService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    /**
     * Retrieves the current account balance and credit information.
     * @return an AccountBalance object containing balance and credit details
     */
    public AccountBalance getBalance() {
        CurrentUser user = loadCurrentUser();
        return new AccountBalance(user.balance(), user.credit());
    }

    /**
     * Loads the current user information from the API.
     * @return a CurrentUser object representing the authenticated user
     */
    private CurrentUser loadCurrentUser() {
        VastAIRequest request = client.requestBuilder()
                .get()
                .path("/users/current/")
                .build();
        currentUser = client.execute(request, CurrentUser.class);
        return currentUser;
    }

    @Override
    public void update() {

    }

    /**
     * Retrieves the current user information.
     * @return a CurrentUser object representing the authenticated user
     */
    public CurrentUser getClient() {
        return loadCurrentUser();
    }
}
