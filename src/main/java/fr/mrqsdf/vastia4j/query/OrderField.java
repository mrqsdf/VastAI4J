package fr.mrqsdf.vastia4j.query;

/**
 * Champs de tri. La CLI autorise de trier sur plusieurs des champs "offers".
 * On inclut aussi "score" (valeur par défaut côté CLI : score desc).
 */
public enum OrderField {
    SCORE("score"),
    // Les suivants sont pratiques en tri :
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
    OrderField(String json) { this.json = json; }
    public String json() { return json; }

    public static OrderField fromJsonOrNull(String key) {
        for (OrderField f : values()) if (f.json.equals(key)) return f;
        return null;
    }
}
