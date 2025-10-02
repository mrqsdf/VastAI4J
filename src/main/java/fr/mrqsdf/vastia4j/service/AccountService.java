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

    public AccountService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    public AccountBalance getBalance() {
        CurrentUser user = loadCurrentUser();
        return new AccountBalance(user.getBalance(), user.getCredit());
    }

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

    public CurrentUser getClient() {
        return loadCurrentUser();
    }
}
