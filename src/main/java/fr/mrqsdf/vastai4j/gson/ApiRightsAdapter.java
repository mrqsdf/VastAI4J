package fr.mrqsdf.vastai4j.gson;

import com.google.gson.*;
import fr.mrqsdf.vastai4j.auth.ApiRights;
import fr.mrqsdf.vastai4j.auth.EndpointMethods;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Custom deserializer for {@link ApiRights}, which can contain both known categories and dynamic
 * scope entries.
 */
public final class ApiRightsAdapter implements JsonDeserializer<ApiRights> {
    // Known categories documented under "Permissions and authorization"; everything else is treated
    // as a dynamic scope entry.
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
        if (!json.isJsonObject()) throw new JsonParseException("ApiRights expects a JSON object");
        JsonObject obj = json.getAsJsonObject();

        for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
            String key = e.getKey();
            JsonElement val = e.getValue();

            if (KNOWN_CATEGORIES.contains(key)) {
                // Category: store the JSON payload as-is (empty or with constraints).
                if (val.isJsonObject()) {
                    out.getCategories().put(key, val.getAsJsonObject());
                } else if (val.isJsonNull()) {
                    out.getCategories().put(key, new JsonObject());
                } else {
                    // Be tolerant: if the payload is not an object, wrap it so it can be inspected.
                    JsonObject wrap = new JsonObject();
                    wrap.add("value", val);
                    out.getCategories().put(key, wrap);
                }
            } else {
                // Dynamic scope (e.g. "*")
                if (!val.isJsonObject()) continue; // Ignore unexpected primitives.
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
