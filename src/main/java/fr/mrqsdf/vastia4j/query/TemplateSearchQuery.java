package fr.mrqsdf.vastia4j.query;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Builder for {@code GET /api/v0/template/} queries.
 * Supported parameters mirror the official documentation:
 * <ul>
 *     <li>{@code query}: free-form string matching template fields</li>
 *     <li>{@code order_by}: column name used for ordering</li>
 *     <li>{@code select_filters}: server-side filter object encoded as JSON</li>
 * </ul>
 */
public final class TemplateSearchQuery {

    private String query;
    private String orderBy;
    private final Map<String, Object> selectFilters = new LinkedHashMap<>();

    public TemplateSearchQuery query(String q) {
        this.query = q;
        return this;
    }

    /**
     * Specifies the order column accepted by the API (e.g. {@code name}, {@code created_at}).
     */
    public TemplateSearchQuery orderBy(String column) {
        this.orderBy = column;
        return this;
    }


    public String query() {
        return this.query;
    }

    public String orderBy() {
        return this.orderBy;
    }

    /**
     * Adds an arbitrary server-side filter that will be serialized within {@code select_filters}.
     */
    public TemplateSearchQuery addFilter(String key, Object value) {
        Objects.requireNonNull(key, "key");
        selectFilters.put(key, value);
        return this;
    }

    /**
     * Helper that narrows the search to the caller's templates.
     * Vast.ai deployments sometimes expose different keys (e.g. {@code mine}, {@code owner_id}),
     * so adjust the filter name if your control plane expects an alternate field.
     */
    public TemplateSearchQuery personalOnly() {
        // Use "mine" by default; adapt as needed depending on the backend configuration.
        return addFilter("mine", true);
    }

    /**
     * Converts the current state into query parameters suitable for the HTTP request.
     */
    public Map<String, String> toQueryParams(Gson gson) {
        Map<String, String> params = new LinkedHashMap<>();
        if (query != null && !query.isBlank()) params.put("query", query);
        if (orderBy != null && !orderBy.isBlank()) params.put("order_by", orderBy);
        if (!selectFilters.isEmpty()) {
            // select_filters expects a JSON object → serialize deterministically.
            JsonObject sf = (JsonObject) gson.toJsonTree(selectFilters);
            params.put("select_filters", sf.toString());
        }
        return params;
    }

    public String selectFiltersAsRawJson(Gson gson) {
        if (selectFilters.isEmpty()) return null;
        return gson.toJson(selectFilters);
    }
}
