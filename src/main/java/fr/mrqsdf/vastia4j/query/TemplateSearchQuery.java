package fr.mrqsdf.vastia4j.query;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Builder pour l’endpoint GET /api/v0/template/
 * Paramètres supportés (cf. doc) :
 *   - query: string libre pour matcher des champs de template
 *   - order_by: nom de colonne pour le tri
 *   - select_filters: objet (clé -> valeur) appliqué côté serveur
 */
public final class TemplateSearchQuery {

    private String query;
    private String orderBy;
    private final Map<String, Object> selectFilters = new LinkedHashMap<>();

    public TemplateSearchQuery query(String q) { this.query = q; return this; }

    /** Colonne de tri telle qu’acceptée par l’API (ex: "name", "created_at", etc.). */
    public TemplateSearchQuery orderBy(String column) { this.orderBy = column; return this; }


    public String query()   { return this.query; }
    public String orderBy() { return this.orderBy; }

    /** Ajoute un filtre serveur générique. La clé/valeur est envoyée dans select_filters. */
    public TemplateSearchQuery addFilter(String key, Object value) {
        Objects.requireNonNull(key, "key");
        selectFilters.put(key, value);
        return this;
    }

    /**
     * Helper: restreint aux templates « personnels ».
     * NOTE: le nom exact de la clé dépend de l’API (voir doc GET /template/ et Postman).
     * Si ta version n’accepte pas "mine", remplace par la clé supportée côté serveur
     * (exemples courants: "owner_id", "owned_by_me", etc.).
     */
    public TemplateSearchQuery personalOnly() {
        // Choix par défaut: booléen "mine": true (simple et courant)
        // Adapte la clé si besoin (voir notes plus bas).
        return addFilter("mine", true);
    }

    /** Convertit en Map<String,String> pour générer la query-string. */
    public Map<String, String> toQueryParams(Gson gson) {
        Map<String, String> params = new LinkedHashMap<>();
        if (query != null && !query.isBlank()) params.put("query", query);
        if (orderBy != null && !orderBy.isBlank()) params.put("order_by", orderBy);
        if (!selectFilters.isEmpty()) {
            // select_filters attend un objet JSON -> on sérialise proprement
            JsonObject sf = (JsonObject) gson.toJsonTree(selectFilters);
            params.put("select_filters", sf.toString());
        }
        return params;
    }

    public String selectFiltersAsRawJson(Gson gson) {
        if (selectFilters.isEmpty()) return null;  // pas de param vide !
        return gson.toJson(selectFilters);         // ex: {"owner_id":123}
    }
}
