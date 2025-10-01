package fr.mrqsdf.vastia4j.query;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Builder that helps constructing complex offer query parameters.
 */
public class OfferQuery {

    private final Map<String, Object> parameters = new LinkedHashMap<>();

    public static OfferQuery create() {
        return new OfferQuery();
    }

    private OfferQuery() {
    }

    public OfferQuery minVcpu(Number value) {
        return put("min_vcpu", value);
    }

    public OfferQuery maxPrice(Number value) {
        return put("max_price", value);
    }

    public OfferQuery minGpuMem(Number value) {
        return put("min_gpu_ram", value);
    }

    public OfferQuery verified(Boolean verified) {
        return put("verified", verified);
    }

    public OfferQuery type(String type) {
        return put("type", type);
    }

    public OfferQuery with(String key, Object value) {
        return put(key, value);
    }

    public Map<String, String> toQueryParams() {
        Map<String, String> params = new LinkedHashMap<>();
        parameters.forEach((key, value) -> {
            if (value != null) {
                params.put(key, String.valueOf(value));
            }
        });
        return params;
    }

    private OfferQuery put(String key, Object value) {
        Objects.requireNonNull(key, "key");
        if (value == null) {
            parameters.remove(key);
        } else {
            parameters.put(key, value);
        }
        return this;
    }
}
