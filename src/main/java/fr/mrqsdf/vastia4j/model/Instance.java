package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

/**
 * Representation of a Vast.ai instance.
 */
public class Instance {

    @SerializedName("id")
    private long id;

    @SerializedName("machine_id")
    private long machineId;

    @SerializedName("image")
    private String image;

    @SerializedName("label")
    private String label;

    @SerializedName("status")
    private String status;

    @SerializedName("hostname")
    private String hostname;

    @SerializedName("public_ip")
    private String publicIp;

    @SerializedName("ssh_port")
    private Integer sshPort;

    @SerializedName("jupyter_url")
    private String jupyterUrl;

    @SerializedName("onstart")
    private String onstart;

    public long getId() {
        return id;
    }

    public long getMachineId() {
        return machineId;
    }

    public String getImage() {
        return image;
    }

    public String getLabel() {
        return label;
    }

    public String getStatus() {
        return status;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public Integer getSshPort() {
        return sshPort;
    }

    public String getJupyterUrl() {
        return jupyterUrl;
    }

    public String getOnstart() {
        return onstart;
    }
}
