package fr.mrqsdf.vastia4j.service;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.http.VastAIRequest;
import fr.mrqsdf.vastia4j.model.Offer;
import fr.mrqsdf.vastia4j.model.OfferListResponse;
import fr.mrqsdf.vastia4j.query.OfferQuery;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * Service that exposes the offer search endpoints described in the Vast.ai API documentation.
 * <p>
 * The legacy search still relies on {@code POST /bundles/} while the newer search mirrors the
 * console experience via {@code PUT /search/asks/}. See the "Search offers" chapter of the
 * official documentation for details on the available filters and payload formats.
 * </p>
 */
public final class OfferService implements Service {

    private final VastAIClient client;

    public OfferService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    /**
     * Executes the historical {@code POST /bundles/} endpoint (note the base path already
     * contains {@code /api/v0}, so the trailing segment must be {@code /bundles/}).
     */
    public List<Offer> search(OfferQuery query) {
        JsonObject body = query.toQueryJson();

        VastAIRequest req = client.requestBuilder()
                .post()
                .path("/bundles/")
                .body(body)
                .build();


        OfferListResponse resp = client.execute(req, OfferListResponse.class);
        return resp != null && resp.offers() != null ? resp.offers() : List.of();
    }

    /**
     * Executes the modern {@code PUT /search/asks/} endpoint that returns the same payload as the
     * Vast.ai web console. The request body combines the serialized query with a default
     * {@code select_cols} entry containing {@code "*"}.
     */
    public List<Offer> searchNew(OfferQuery query) {
        JsonObject body = query.toSearchAsksPayload();

        VastAIRequest req = client.requestBuilder()
                .put()
                .path("/search/asks/")
                .body(body)
                .build();

        // Some deployments reply with a wrapper, others return the raw array.
        OfferListResponse wrapped = client.execute(req, OfferListResponse.class);
        if (wrapped != null && wrapped.offers() != null) return wrapped.offers();

        Type listType = new TypeToken<List<Offer>>(){}.getType();
        List<Offer> direct = client.execute(req, listType);
        return direct != null ? direct : List.of();
    }

    @Override public void update() { /* no-op */ }
}
