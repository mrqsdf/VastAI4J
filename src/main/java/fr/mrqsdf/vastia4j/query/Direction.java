package fr.mrqsdf.vastia4j.query;

/**
 * Enum representing sorting directions for queries.
 */
public enum Direction {
    ASC("asc"), DESC("desc");
    private final String json;

    Direction(String json) {
        this.json = json;
    }

    /**
     * Returns the JSON representation of the direction.
     *
     * @return the JSON string ("asc" or "desc").
     */
    public String json() {
        return json;
    }
}