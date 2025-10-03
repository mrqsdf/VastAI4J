package fr.mrqsdf.vastia4j.model.instance;

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

    public String json() {
        return json;
    }
}
