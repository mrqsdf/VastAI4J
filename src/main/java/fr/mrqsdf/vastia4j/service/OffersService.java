package fr.mrqsdf.vastia4j.service;

import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.http.VastAIRequest;
import fr.mrqsdf.vastia4j.model.Offer;
import fr.mrqsdf.vastia4j.model.OfferListResponse;
import fr.mrqsdf.vastia4j.query.OfferQuery;

import java.util.Objects;

/**
 * High level helper for interacting with Vast.ai offer endpoints.
 */
public class OffersService {

    private final VastAIClient client;

    public OffersService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    /**
     * Retrieve the best offers (sorted by Vast.ai) with optional filters.
     */
    public OfferListResponse listBestOffers(OfferQuery query) {
        VastAIRequest.Builder builder = client.requestBuilder()
                .get()
                .path("/v0/offers/best/");
        if (query != null) {
            builder.queryParams(query.toQueryParams());
        }
        return client.execute(builder.build(), OfferListResponse.class);
    }

    /**
     * Retrieve raw offers endpoint using the provided path (e.g. /v0/offers/).
     */
    public OfferListResponse listOffers(String path, OfferQuery query) {
        Objects.requireNonNull(path, "path");
        VastAIRequest.Builder builder = client.requestBuilder()
                .get()
                .path(path);
        if (query != null) {
            builder.queryParams(query.toQueryParams());
        }
        return client.execute(builder.build(), OfferListResponse.class);
    }

    /**
     * Fetch a single offer by its identifier.
     */
    public Offer getOffer(long offerId) {
        VastAIRequest request = client.requestBuilder()
                .get()
                .path("/v0/offers/" + offerId + "/")
                .build();
        return client.execute(request, Offer.class);
    }
}
