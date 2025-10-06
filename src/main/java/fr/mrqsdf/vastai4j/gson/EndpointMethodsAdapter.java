package fr.mrqsdf.vastai4j.gson;

import com.google.gson.*;
import fr.mrqsdf.vastai4j.auth.EndpointMethods;
import fr.mrqsdf.vastai4j.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;

/**
 * Gson adapter to deserialize an {@link EndpointMethods} mapping from JSON.
 * <p>
 * The JSON structure is a mapping of HTTP methods (as strings) to objects (which may be empty).
 * Example:
 * <pre>
 * {
 *   "GET": {},
 *   "POST": {
 *     "required_params": ["name", "email"]
 *   },
 *   "DELETE": {}
 * }
 * </pre>
 * This adapter will parse the above into an {@link EndpointMethods} instance mapping
 * {@link HttpMethod#GET}, {@link HttpMethod#POST}, and {@link HttpMethod#DELETE}
 * to their respective JSON objects.
 */
public final class EndpointMethodsAdapter implements JsonDeserializer<EndpointMethods> {

    /**
     * Deserialize a JSON element into an {@link EndpointMethods} instance.
     * @param json the JSON data being deserialized
     * @param typeOfT the type of the Object to deserialize to
     * @param ctx context for deserialization
     * @return the deserialized {@link EndpointMethods} instance
     * @throws JsonParseException if the JSON is not in the expected format
     */
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
