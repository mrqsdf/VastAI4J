package fr.mrqsdf.vastai4j.auth;

import com.google.gson.JsonObject;
import fr.mrqsdf.vastai4j.http.HttpMethod;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents the API rights associated with an API key, including named categories
 * and dynamic scopes mapped to endpoints and their allowed HTTP methods.
 */
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

    /**
     * @return the named categories with their associated JSON objects.
     */
    public Map<String, JsonObject> getCategories() {
        return categories;
    }

    /**
     * @return the dynamic scopes mapped to endpoints and their allowed HTTP methods.
     */
    public Map<String, Map<String, EndpointMethods>> getScopes() {
        return scopes;
    }

    /**
     * Convenience helper that checks whether a given HTTP method is authorized for an endpoint
     * within the provided scope.
     *
     * @param scopeKey the scope key (often {@code "*"}).
     *                 See {@link #scopes} for possible values.
     * @param endpoint the endpoint name (for example, {@code "api.team"} for {@code /api/team}).
     *                 See {@link #scopes} for possible values.
     * @param method   the HTTP method to check (for example, {@link HttpMethod#GET}).
     * @return true if the method is allowed, false otherwise.
     */
    public boolean isAllowed(String scopeKey, String endpoint, HttpMethod method) {
        Map<String, EndpointMethods> byEndpoint = scopes.get(scopeKey);
        if (byEndpoint == null) return false;
        EndpointMethods m = byEndpoint.get(endpoint);
        return m != null && m.allows(method);
    }
}
