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

public final class OfferService implements Service {

    private final VastAIClient client;

    public OfferService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    /** Recommandé historiquement : POST /bundles/  (⚠️ sans /v0 ici) */
    public List<Offer> search(OfferQuery query) {
        JsonObject body = query.toQueryJson();

        VastAIRequest req = client.requestBuilder()
                .post()
                .path("/bundles/")              // <-- FIX: pas de /v0 ici
                .body(body)
                .build();


        OfferListResponse resp = client.execute(req, OfferListResponse.class);
        return resp != null && resp.offers() != null ? resp.offers() : List.of();
    }

    /** Variante récente : PUT /search/asks/ avec {"select_cols":["*"],"q":<query>} */
    public List<Offer> searchNew(OfferQuery query) {
        JsonObject body = query.toSearchAsksPayload();

        VastAIRequest req = client.requestBuilder()
                .put()
                .path("/search/asks/")          // <-- FIX: endpoint documenté
                .body(body)
                .build();

        // selon la version serveur, la réponse peut être un wrapper ou un tableau direct
        OfferListResponse wrapped = client.execute(req, OfferListResponse.class);
        if (wrapped != null && wrapped.offers() != null) return wrapped.offers();

        Type listType = new TypeToken<List<Offer>>(){}.getType();
        List<Offer> direct = client.execute(req, listType);
        return direct != null ? direct : List.of();
    }

    @Override public void update() { /* no-op */ }
}
