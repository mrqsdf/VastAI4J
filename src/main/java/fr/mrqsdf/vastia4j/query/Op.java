package fr.mrqsdf.vastia4j.query;

/**
 * Opérateurs supportés par l’API Vast pour la recherche d’offres.
 */
public enum Op {
    LT("lt"), LE("le"), EQ("eq"), NE("neq"), GE("ge"), GT("gt"),
    IN("in"), NOT_IN("notin");

    private final String jsonKey;

    Op(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public String jsonKey() {
        return jsonKey;
    }
}
