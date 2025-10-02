package fr.mrqsdf.vastia4j.auth;

import com.google.gson.JsonObject;
import fr.mrqsdf.vastia4j.http.HttpMethod;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ApiRights {
    /**
     * Catégories définies par Vast (clé = nom catégorie, valeur = JSON libre/contraintes).
     * Exemples de clés possibles (non exhaustif) : user_read, user_write, instance_read, instance_write,
     * billing_read, billing_write, machine_read, machine_write, team_read, team_write, misc, ...
     * Réf. : doc "Permissions-and-authorization".
     */
    private final Map<String, JsonObject> categories = new LinkedHashMap<>();

    /**
     * Scopes dynamiques (souvent "*") → endpoints → méthodes autorisées.
     * Exemple :
     *   scopes.get("*").get("api.team").allows(HttpMethod.POST) == true
     */
    private final Map<String, Map<String, EndpointMethods>> scopes = new LinkedHashMap<>();

    public Map<String, JsonObject> getCategories() { return categories; }
    public Map<String, Map<String, EndpointMethods>> getScopes() { return scopes; }

    /**
     * Helper pratique : savoir si une méthode est autorisée pour un endpoint dans un scope.
     */
    public boolean isAllowed(String scopeKey, String endpoint, HttpMethod method) {
        Map<String, EndpointMethods> byEndpoint = scopes.get(scopeKey);
        if (byEndpoint == null) return false;
        EndpointMethods m = byEndpoint.get(endpoint);
        return m != null && m.allows(method);
    }
}
