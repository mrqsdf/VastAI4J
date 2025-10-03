package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

/**
 * Modèle d’une "offer" (ask/contract) renvoyée par la recherche Vast.
 * Les champs reflètent ceux exposés par la CLI (cf. docs "search offers").
 * Ajoute/enlève librement selon tes besoins : Gson ignore les champs inconnus/absents.
 */
public record Offer(
        Long id,
        @SerializedName("machine_id") Long machineId,
        @SerializedName("gpu_name") String gpuName,
        @SerializedName("gpu_arch") String gpuArch,
        @SerializedName("num_gpus") Integer numGpus,
        @SerializedName("gpu_ram") Double gpuRamGiB,
        @SerializedName("gpu_total_ram") Double gpuTotalRamGiB,
        @SerializedName("cuda_vers") Double cudaVers,
        @SerializedName("driver_version") String driverVersion,
        @SerializedName("cpu_arch") String cpuArch,
        @SerializedName("cpu_cores") Integer cpuCores,
        @SerializedName("cpu_cores_effective") Double cpuCoresEffective,
        @SerializedName("cpu_ghz") Double cpuGhz,
        @SerializedName("cpu_ram") Double cpuRamGiB,
        @SerializedName("disk_space") Double diskSpaceGiB,
        @SerializedName("disk_bw") Double diskBandwidthMBs,
        @SerializedName("inet_up") Double inetUpMbps,
        @SerializedName("inet_down") Double inetDownMbps,
        @SerializedName("inet_up_cost") Double inetUpCostPerGB,
        @SerializedName("inet_down_cost") Double inetDownCostPerGB,
        @SerializedName("dlperf") Double dlperf,
        @SerializedName("dlperf_usd") Double dlperfUsd,
        @SerializedName("flops_usd") Double flopsUsd,
        @SerializedName("dph") Double pricePerHourUSD,
        @SerializedName("dlperf_per_dphtotal") Double dlperfPerDphTotal,
        @SerializedName("dph_base") Double dphBase,
        @SerializedName("dph_total") Double dphTotal,
        @SerializedName("min_bid") Double minBidUSD,
        @SerializedName("reliability") Double reliability,
        @SerializedName("duration") Double maxDurationDays,
        @SerializedName("geolocation") String geoCountryCode,
        @SerializedName("datacenter") Boolean datacenter,
        @SerializedName("verified") Boolean verified,
        @SerializedName("rentable") Boolean rentable,
        @SerializedName("rented") Boolean rented,
        @SerializedName("static_ip") Boolean staticIp,
        @SerializedName("vms_enabled") Boolean vmsEnabled,
        @SerializedName("pci_gen") Double pciGen,
        @SerializedName("pcie_bw") Double pcieBwGBs,
        @SerializedName("bw_nvlink") Double bwNvlinkGBs
) {
}
