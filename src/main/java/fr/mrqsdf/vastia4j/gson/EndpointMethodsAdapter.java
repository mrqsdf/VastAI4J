package fr.mrqsdf.vastia4j.gson;

import com.google.gson.*;
import fr.mrqsdf.vastia4j.auth.EndpointMethods;
import fr.mrqsdf.vastia4j.http.HttpMethod;
import fr.mrqsdf.vastia4j.auth.Right;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;

public final class EndpointMethodsAdapter implements JsonDeserializer<EndpointMethods> {
    @Override
    public EndpointMethods deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
        EndpointMethods out = new EndpointMethods();

        if (!json.isJsonObject()) {
            // Some scopes may be empty objects (no methods listed) – return an empty mapping.
            return out;
        }
        JsonObject obj = json.getAsJsonObject();

        for (Map.Entry<String, JsonElement> methodEntry : obj.entrySet()) {
            String methodStr = methodEntry.getKey();
            HttpMethod method;
            try {
                method = HttpMethod.valueOf(methodStr.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                // Unknown HTTP verb: ignore gracefully.
                continue;
            }

            JsonElement bodyEl = methodEntry.getValue();
            JsonObject body = bodyEl != null && bodyEl.isJsonObject()
                    ? bodyEl.getAsJsonObject()
                    : new JsonObject();

            out.put(method, body);
        }
        return out;
    }
}
