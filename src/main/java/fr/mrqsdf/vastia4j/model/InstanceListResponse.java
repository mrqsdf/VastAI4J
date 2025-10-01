package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Response wrapper for listing Vast.ai instances.
 */
public class InstanceListResponse {

    @SerializedName("instances")
    private List<Instance> instances;

    @SerializedName("total")
    private int total;

    @SerializedName("count")
    private int count;

    @SerializedName("limit")
    private int limit;

    @SerializedName("offset")
    private int offset;

    public List<Instance> getInstances() {
        return instances == null ? Collections.emptyList() : Collections.unmodifiableList(instances);
    }

    public int getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
}
