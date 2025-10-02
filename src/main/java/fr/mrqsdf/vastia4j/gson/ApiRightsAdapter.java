package fr.mrqsdf.vastia4j.gson;

import com.google.gson.*;
import fr.mrqsdf.vastia4j.auth.ApiRights;
import fr.mrqsdf.vastia4j.auth.EndpointMethods;
import fr.mrqsdf.vastia4j.auth.Right;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class ApiRightsAdapter implements JsonDeserializer<ApiRights> {
    // Liste des catégories connues, d’après la doc “Permissions-and-authorization”.
    // (On reste tolérant : tout le reste est interprété comme un scope dynamique.)
    private static final Set<String> KNOWN_CATEGORIES = Set.of(
            "misc",
            "user_read", "user_write",
            "instance_read", "instance_write",
            "billing_read", "billing_write",
            "machine_read", "machine_write",
            "team_read", "team_write"
    );

    @Override
    public ApiRights deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
        ApiRights out = new ApiRights();
        if (!json.isJsonObject()) throw new JsonParseException("ApiRights attend un objet JSON");
        JsonObject obj = json.getAsJsonObject();

        for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
            String key = e.getKey();
            JsonElement val = e.getValue();

            if (KNOWN_CATEGORIES.contains(key)) {
                // Catégorie : on stocke tel quel (objet JSON, vide ou avec contraintes)
                if (val.isJsonObject()) {
                    out.getCategories().put(key, val.getAsJsonObject());
                } else if (val.isJsonNull()) {
                    out.getCategories().put(key, new JsonObject());
                } else {
                    // tolérance : si ce n'est pas un objet, on l’encapsule
                    JsonObject wrap = new JsonObject();
                    wrap.add("value", val);
                    out.getCategories().put(key, wrap);
                }
            } else {
                // Scope dynamique (ex: "*")
                if (!val.isJsonObject()) continue; // ignore si non-objet
                JsonObject endpointsObj = val.getAsJsonObject();
                Map<String, EndpointMethods> endpoints = new LinkedHashMap<>();
                for (Map.Entry<String, JsonElement> ep : endpointsObj.entrySet()) {
                    String endpointName = ep.getKey();
                    JsonElement epVal = ep.getValue();
                    EndpointMethods parsed = ctx.deserialize(epVal, EndpointMethods.class);
                    endpoints.put(endpointName, parsed);
                }
                out.getScopes().put(key, endpoints);
            }
        }
        return out;
    }
}
