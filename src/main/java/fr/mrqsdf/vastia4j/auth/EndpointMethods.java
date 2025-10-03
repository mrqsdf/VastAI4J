package fr.mrqsdf.vastia4j.auth;

import com.google.gson.JsonObject;
import fr.mrqsdf.vastia4j.http.HttpMethod;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Set;

public final class EndpointMethods {
    private final EnumMap<HttpMethod, JsonObject> methods = new EnumMap<>(HttpMethod.class);

    public boolean allows(HttpMethod method) {
        return methods.containsKey(method);
    }

    public Set<HttpMethod> allowedMethods() {
        return Collections.unmodifiableSet(methods.keySet());
    }

    public JsonObject methodBody(HttpMethod method) {
        return methods.get(method);
    }

    public void put(HttpMethod m, JsonObject body) {
        methods.put(m, body);
    }
}
