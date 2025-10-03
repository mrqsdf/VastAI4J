package fr.mrqsdf.vastia4j.query;

/**
 * Operators supported by the Vast.ai offer search API.
 */
public enum Op {
    LT("lt"), LE("le"), EQ("eq"), NE("neq"), GE("ge"), GT("gt"),
    IN("in"), NOT_IN("notin");

    private final String jsonKey;

    Op(String jsonKey) {
        this.jsonKey = jsonKey;
    }
    /**
     * Get the JSON key corresponding to this operator.
     * @return the JSON key as a string.
     */
    public String jsonKey() {
        return jsonKey;
    }
}
