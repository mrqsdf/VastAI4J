package fr.mrqsdf.vastia4j.service;

import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.http.VastAIRequest;
import fr.mrqsdf.vastia4j.model.Instance;
import fr.mrqsdf.vastia4j.model.InstanceListResponse;
import fr.mrqsdf.vastia4j.model.InstanceLaunchRequest;

import java.util.Objects;

/**
 * High level helper for Vast.ai instance management.
 */
public class InstancesService {

    private final VastAIClient client;

    public InstancesService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    public InstanceListResponse listInstances() {
        VastAIRequest request = client.requestBuilder()
                .get()
                .path("/v0/instances/")
                .build();
        return client.execute(request, InstanceListResponse.class);
    }

    public Instance getInstance(long id) {
        VastAIRequest request = client.requestBuilder()
                .get()
                .path("/v0/instances/" + id + "/")
                .build();
        return client.execute(request, Instance.class);
    }

    public Instance launchInstance(InstanceLaunchRequest launchRequest) {
        Objects.requireNonNull(launchRequest, "launchRequest");
        VastAIRequest request = client.requestBuilder()
                .post()
                .path("/v0/instances/")
                .body(launchRequest.toPayload())
                .build();
        return client.execute(request, Instance.class);
    }

    public void deleteInstance(long id) {
        VastAIRequest request = client.requestBuilder()
                .delete()
                .path("/v0/instances/" + id + "/")
                .build();
        client.execute(request, String.class);
    }
}
