package fr.mrqsdf.vastai4j.query;

/**
 * Sortable fields supported by the Vast.ai CLI and API when ordering offer results.
 * The special {@code score} column is included because it is the default ordering used by the CLI.
 */
public enum OrderField {
    SCORE("score"),
    // Additional columns that are handy to expose for ordering:
    NUM_GPUS("num_gpus"),
    DLPERF("dlperf"),
    DLPERF_USD("dlperf_usd"),
    FLOPS_USD("flops_usd"),
    DPH("dph"),
    RELIABILITY("reliability"),
    TOTAL_FLOPS("total_flops"),
    GPU_RAM("gpu_ram"),
    INET_DOWN("inet_down"),
    INET_UP("inet_up");

    private final String json;

    OrderField(String json) {
        this.json = json;
    }

    /**
     * Returns the JSON representation of this order field, as used in API requests.
     *
     * @return the JSON key corresponding to this order field.
     */
    public String json() {
        return json;
    }

    /**
     * Parses a JSON key and returns the corresponding OrderField enum value.
     *
     * @param key the JSON key to parse.
     * @return the corresponding OrderField, or null if the key is unrecognized.
     */
    public static OrderField fromJsonOrNull(String key) {
        for (OrderField f : values()) if (f.json.equals(key)) return f;
        return null;
    }
}
