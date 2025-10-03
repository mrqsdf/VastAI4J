package fr.mrqsdf.vastia4j.service;

import com.google.gson.Gson;
import fr.mrqsdf.vastia4j.client.VastAIClient;
import fr.mrqsdf.vastia4j.http.VastAIRequest;
import fr.mrqsdf.vastia4j.model.Template;
import fr.mrqsdf.vastia4j.model.TemplateSearchResponse;
import fr.mrqsdf.vastia4j.query.TemplateSearchQuery;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service that wraps the template discovery endpoints from the Vast.ai API reference.
 * It supports the {@code GET /api/v0/template/} family of filters including {@code query},
 * {@code order_by} and {@code select_filters} as described in the official documentation.
 */
public final class TemplateService implements Service {

    private final VastAIClient client;

    public TemplateService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    /**
     * Searches templates using the {@code GET /api/v0/template/} endpoint with optional
     * {@code query}, {@code order_by} and {@code select_filters} parameters.
     */
    public List<Template> search(TemplateSearchQuery q) {
        var gson = client.getGson();
        var b = client.requestBuilder().get().path("/template/");

        // Simple pass-through parameters for query and ordering.
        if (q.query() != null && !q.query().isBlank())
            b.addQueryParam("query", q.query());
        if (q.orderBy() != null && !q.orderBy().isBlank())
            b.addQueryParam("order_by", q.orderBy());

        // Select filters are pre-serialized once so the client can URL-encode the JSON payload.
        String sf = q.selectFiltersAsRawJson(gson);
        if (sf != null) b.addQueryParam("select_filters", sf);

        var resp = client.execute(b.build(), TemplateSearchResponse.class);
        return (resp == null || resp.templates() == null) ? List.of() : new ArrayList<>(resp.templates());
    }

    /**
     * Searches templates using the {@code GET /api/v0/template/} endpoint with optional
     * {@code query} and {@code order_by} parameters.
     * This is a convenience helper that does not support {@code select_filters}.
     * @return the list of templates matching the search criteria, or an empty list if none found.
     */
    public List<Template> searchAll(String qStr, String orderBy) {
        VastAIRequest.Builder b = client.requestBuilder()
                .get()
                .path("/template/");

        if (qStr != null && !qStr.isBlank()) b.addQueryParam("query", qStr);
        if (orderBy != null && !orderBy.isBlank()) b.addQueryParam("order_by", orderBy);

        VastAIRequest req = b.build();
        TemplateSearchResponse resp = client.execute(req, TemplateSearchResponse.class);
        return (resp == null || resp.templates() == null) ? List.of() : new ArrayList<>(resp.templates());
    }

    private static String urlenc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    /**
     * Convenience helper to retrieve the authenticated user's templates.
     * @param optionalQuery optional search query (can be null or blank)
     * @param orderBy optional order-by clause (can be null or blank)
     * @return the list of templates owned by the authenticated user, or an empty list if none found.
     */
    public List<Template> searchMyTemplates(String optionalQuery, String orderBy) {
        TemplateSearchQuery q = new TemplateSearchQuery().personalOnly();
        if (optionalQuery != null && !optionalQuery.isBlank()) q.query(optionalQuery);
        if (orderBy != null && !orderBy.isBlank()) q.orderBy(orderBy);
        return search(q);
    }

    /**
     * Convenience helper to retrieve a specific user's templates by their user ID.
     * @param myUserId the user ID whose templates to retrieve
     * @param optionalQuery optional search query (can be null or blank)
     * @param orderBy optional order-by clause (can be null or blank)
     * @return the list of templates owned by the specified user, or an empty list if none found.
     */
    public List<Template> searchMyTemplates(long myUserId, String optionalQuery, String orderBy) {
        TemplateSearchQuery q = new TemplateSearchQuery().addFilter("owner_id", myUserId);
        if (optionalQuery != null && !optionalQuery.isBlank()) q.query(optionalQuery);
        if (orderBy != null && !orderBy.isBlank()) q.orderBy(orderBy);
        return search(q);
    }

    @Override
    public void update() { /* no-op */ }
}
