package fr.mrqsdf.vastia4j.query;

public enum Direction {
    ASC("asc"), DESC("desc");
    private final String json;

    Direction(String json) {
        this.json = json;
    }

    public String json() {
        return json;
    }
}