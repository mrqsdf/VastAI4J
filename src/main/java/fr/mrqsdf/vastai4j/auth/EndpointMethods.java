package fr.mrqsdf.vastai4j.auth;

import com.google.gson.JsonObject;
import fr.mrqsdf.vastai4j.http.HttpMethod;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Set;

/**
 * Represents the allowed HTTP methods for a specific API endpoint along with their associated request bodies.
 */
public final class EndpointMethods {
    private final EnumMap<HttpMethod, JsonObject> methods = new EnumMap<>(HttpMethod.class);

    /**
     * Checks if the specified HTTP method is allowed for this endpoint.
     * @param method the HTTP method to check.
     * @return true if the method is allowed, false otherwise.
     */
    public boolean allows(HttpMethod method) {
        return methods.containsKey(method);
    }

    /**
     * @return an unmodifiable set of allowed HTTP methods for this endpoint.
     */
    public Set<HttpMethod> allowedMethods() {
        return Collections.unmodifiableSet(methods.keySet());
    }

    /**
     * Retrieves the request body associated with the specified HTTP method.
     * @param method the HTTP method whose body is to be retrieved.
     * @return the JsonObject representing the request body, or null if no body is associated.
     */
    public JsonObject methodBody(HttpMethod method) {
        return methods.get(method);
    }

    /**
     * Associates a request body with a specific HTTP method for this endpoint.
     * @param m the HTTP method to associate the body with.
     * @param body the JsonObject representing the request body.
     */
    public void put(HttpMethod m, JsonObject body) {
        methods.put(m, body);
    }
}
