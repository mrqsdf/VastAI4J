package fr.mrqsdf.vastia4j.auth;

import com.google.gson.JsonObject;
import fr.mrqsdf.vastia4j.http.HttpMethod;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ApiRights {
    /**
     * Named categories returned by Vast.ai (key = category name, value = JSON constraints/metadata).
     * Examples include {@code user_read}, {@code user_write}, {@code instance_read},
     * {@code instance_write}, {@code billing_read}, {@code billing_write}, {@code machine_read},
     * {@code machine_write}, {@code team_read}, {@code team_write}, {@code misc}, etc.
     * See the "Permissions and authorization" section of the official documentation.
     */
    private final Map<String, JsonObject> categories = new LinkedHashMap<>();

    /**
     * Dynamic scopes (often {@code *}) mapped to endpoints and their allowed HTTP methods.
     * Example: {@code scopes.get("*").get("api.team").allows(HttpMethod.POST)} would return true
     * when the authenticated key can POST to {@code /api/team}.
     */
    private final Map<String, Map<String, EndpointMethods>> scopes = new LinkedHashMap<>();

    public Map<String, JsonObject> getCategories() {
        return categories;
    }

    public Map<String, Map<String, EndpointMethods>> getScopes() {
        return scopes;
    }

    /**
     * Convenience helper that checks whether a given HTTP method is authorized for an endpoint
     * within the provided scope.
     */
    public boolean isAllowed(String scopeKey, String endpoint, HttpMethod method) {
        Map<String, EndpointMethods> byEndpoint = scopes.get(scopeKey);
        if (byEndpoint == null) return false;
        EndpointMethods m = byEndpoint.get(endpoint);
        return m != null && m.allows(method);
    }
}
