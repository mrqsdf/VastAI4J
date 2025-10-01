package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

/**
 * Representation of a Vast.ai offer.
 */
public class Offer {

    @SerializedName("id")
    private long id;

    @SerializedName("machine_id")
    private long machineId;

    @SerializedName("gpu_name")
    private String gpuName;

    @SerializedName("num_gpus")
    private int gpuCount;

    @SerializedName("dph_total")
    private double pricePerHour;

    @SerializedName("min_bid_price")
    private double minBidPrice;

    @SerializedName("cpu_cores")
    private double cpuCores;

    @SerializedName("mem_gib")
    private double memoryGiB;

    @SerializedName("disk_gib")
    private double diskGiB;

    @SerializedName("bandwidth")
    private double bandwidth;

    public long getId() {
        return id;
    }

    public long getMachineId() {
        return machineId;
    }

    public String getGpuName() {
        return gpuName;
    }

    public int getGpuCount() {
        return gpuCount;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public double getMinBidPrice() {
        return minBidPrice;
    }

    public double getCpuCores() {
        return cpuCores;
    }

    public double getMemoryGiB() {
        return memoryGiB;
    }

    public double getDiskGiB() {
        return diskGiB;
    }

    public double getBandwidth() {
        return bandwidth;
    }
}
