package fr.mrqsdf.vastia4j.service;

import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.http.VastAIRequest;
import fr.mrqsdf.vastia4j.model.AccountBalance;
import fr.mrqsdf.vastia4j.model.AccountInfo;

import java.util.Objects;

/**
 * Account oriented helper for Vast.ai endpoints.
 */
public class AccountService {

    private final VastAIClient client;

    public AccountService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    public AccountInfo getAccount() {
        VastAIRequest request = client.requestBuilder()
                .get()
                .path("/v0/account/")
                .build();
        return client.execute(request, AccountInfo.class);
    }

    public AccountBalance getBalance() {
        VastAIRequest request = client.requestBuilder()
                .get()
                .path("/v0/billing/balance/")
                .build();
        return client.execute(request, AccountBalance.class);
    }
}
