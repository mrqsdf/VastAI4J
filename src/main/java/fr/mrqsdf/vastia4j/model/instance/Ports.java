package fr.mrqsdf.vastia4j.model.instance;

import com.google.gson.annotations.SerializedName;

public record Ports(@SerializedName("HostIp") String hostIp, @SerializedName("HostPort") int hostPort) {
}
