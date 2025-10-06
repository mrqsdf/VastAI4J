package fr.mrqsdf.vastai4j.model.instance;

/**
 * Enumeration of possible run types for instances on Vast.ai.
 */
public enum RunType {
    SSH("ssh"),
    JUPYTER("jupyter"),
    ARGS("args"),
    SSH_PROXY("ssh_proxy"),
    SSH_DIRECT("ssh_direct"),
    JUPYTER_PROXY("jupyter_proxy"),
    JUPYTER_DIRECT("jupyter_direct");

    private final String json;

    RunType(String json) {
        this.json = json;
    }

    /**
     * Returns the JSON representation of the run type.
     * @return the JSON string corresponding to the run type
     */
    public String json() {
        return json;
    }
}
