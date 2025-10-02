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

public final class TemplateService implements Service {

    private final VastAIClient client;

    public TemplateService(VastAIClient client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    /** GET /api/v0/template/?query=...&order_by=...&select_filters={...} */
    public List<Template> search(TemplateSearchQuery q) {
        var gson = client.getGson();
        var b = client.requestBuilder().get().path("/template/");

        // query / order_by (simples)
        if (q.query() != null && !q.query().isBlank())
            b.addQueryParam("query", q.query());
        if (q.orderBy() != null && !q.orderBy().isBlank())
            b.addQueryParam("order_by", q.orderBy());

        // select_filters : JSON sérialisé, puis encodé par VastAIClient (une seule fois)
        String sf = q.selectFiltersAsRawJson(gson);  // null si vide
        if (sf != null) b.addQueryParam("select_filters", sf);

        var resp = client.execute(b.build(), TemplateSearchResponse.class);
        return (resp == null || resp.templates() == null) ? List.of() : new ArrayList<>(resp.templates());
    }

    public List<Template> searchAll(String qStr, String orderBy) {
        VastAIRequest.Builder b = client.requestBuilder()
                .get()
                .path("/template/"); // base URL = https://console.vast.ai/api/v0

        if (qStr != null && !qStr.isBlank()) b.addQueryParam("query", qStr);
        if (orderBy != null && !orderBy.isBlank()) b.addQueryParam("order_by", orderBy);

        VastAIRequest req = b.build();
        TemplateSearchResponse resp = client.execute(req, TemplateSearchResponse.class);
        return (resp == null || resp.templates() == null) ? List.of() : new ArrayList<>(resp.templates());
    }

    private static String urlenc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    /** Helpers “mes templates” */
    public List<Template> searchMyTemplates(String optionalQuery, String orderBy) {
        TemplateSearchQuery q = new TemplateSearchQuery().personalOnly();
        if (optionalQuery != null && !optionalQuery.isBlank()) q.query(optionalQuery);
        if (orderBy != null && !orderBy.isBlank()) q.orderBy(orderBy);
        return search(q);
    }

    public List<Template> searchMyTemplates(long myUserId, String optionalQuery, String orderBy) {
        TemplateSearchQuery q = new TemplateSearchQuery().addFilter("owner_id", myUserId);
        if (optionalQuery != null && !optionalQuery.isBlank()) q.query(optionalQuery);
        if (orderBy != null && !orderBy.isBlank()) q.orderBy(orderBy);
        return search(q);
    }

    @Override public void update() { /* no-op */ }
}
