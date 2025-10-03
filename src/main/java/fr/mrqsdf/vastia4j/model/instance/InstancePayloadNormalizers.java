package fr.mrqsdf.vastia4j.model.instance;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.*;

public final class InstancePayloadNormalizers {
    private InstancePayloadNormalizers() {}

    /** image_args → List<String> (accepte String ou Array<String>) */
    public static List<String> imageArgsAsList(JsonElement imageArgs) {
        if (imageArgs == null || imageArgs.isJsonNull()) return List.of();
        if (imageArgs.isJsonPrimitive()) {
            String s = imageArgs.getAsString().trim();
            if (s.isEmpty()) return List.of();
            // découpe simple par espaces (ajuste si tu veux gérer les quotes)
            return Arrays.asList(s.split("\\s+"));
        }
        if (imageArgs.isJsonArray()) {
            List<String> out = new ArrayList<>();
            for (JsonElement e : imageArgs.getAsJsonArray()) {
                if (e != null && !e.isJsonNull()) out.add(e.getAsString());
            }
            return out;
        }
        // dernier recours: stringification
        return List.of(imageArgs.toString());
    }

    /** extra_env → Map<String,String> (accepte ["K=V", ...] OU [["K","V"], ...]) */
    public static Map<String,String> extraEnvAsMap(List<JsonElement> raw) {
        Map<String,String> out = new LinkedHashMap<>();
        if (raw == null) return out;

        for (JsonElement el : raw) {
            if (el == null || el.isJsonNull()) continue;

            if (el.isJsonPrimitive()) {
                String s = el.getAsString();
                int idx = s.indexOf('=');
                if (idx > 0) out.put(s.substring(0, idx), s.substring(idx + 1));
            } else if (el.isJsonArray()) {
                JsonArray arr = el.getAsJsonArray();
                if (arr.size() >= 2) {
                    JsonElement k = arr.get(0), v = arr.get(1);
                    out.put(String.valueOf(asString(k)), String.valueOf(asString(v)));
                }
            } else {
                // fallback
                String s = el.toString();
                int idx = s.indexOf('=');
                if (idx > 0) out.put(s.substring(0, idx), s.substring(idx + 1));
            }
        }
        return out;
    }

    private static String asString(JsonElement e) {
        if (e == null || e.isJsonNull()) return "";
        if (e.isJsonPrimitive()) return e.getAsString();
        return e.toString();
    }
}
