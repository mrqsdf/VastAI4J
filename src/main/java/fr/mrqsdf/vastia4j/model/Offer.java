package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

/**
 * Data model describing an offer (ask/contract) returned by Vast.ai search endpoints.
 * The exposed properties mirror those surfaced by the CLI "search offers" command.
 * Additional fields can be added or removed without breaking deserialization because Gson ignores
 * unknown or missing properties.
 * @param id the unique identifier of the offer.
 * @param machineId the ID of the machine providing the offer.
 * @param gpuName the name of the GPU.
 * @param gpuArch the architecture of the GPU.
 * @param numGpus the number of GPUs.
 * @param gpuRamGiB the RAM per GPU in GiB.
 * @param gpuTotalRamGiB the total GPU RAM in GiB.
 * @param cudaVers the CUDA version.
 * @param driverVersion the driver version.
 * @param cpuArch the CPU architecture.
 * @param cpuCores the number of CPU cores.
 * @param cpuCoresEffective the effective number of CPU cores.
 * @param cpuGhz the CPU clock speed in GHz.
 * @param cpuRamGiB the CPU RAM in GiB.
 * @param diskSpaceGiB the disk space in GiB.
 * @param diskBandwidthMBs the disk bandwidth in MB/s.
 * @param inetUpMbps the internet upload speed in Mbps.
 * @param inetUpCostPerGB the internet upload cost per GB.
 * @param inetDownCostPerGB the internet download cost per GB.
 * @param dlperf the DLPerf score.
 * @param dlperfUsd the DLPerf per USD.
 * @param flopsUsd the FLOPS per USD.
 * @param pricePerHourUSD the price per hour in USD.
 * @param dlperfPerDphTotal the DLPerf per total DPH.
 * @param dphBase the base DPH.
 * @param dphTotal the total DPH.
 * @param minBidUSD the minimum bid in USD.
 * @param reliability the reliability score.
 * @param maxDurationDays the maximum duration in days.
 * @param geoCountryCode the geolocation country code.
 * @param datacenter indicates if the offer is from a datacenter.
 * @param verified indicates if the offer is verified.
 * @param rentable  indicates if the offer is rentable.
 * @param rented indicates if the offer is currently rented.
 * @param staticIp indicates if the offer includes a static IP.
 * @param vmsEnabled  indicates if VMs are enabled.
 * @param pciGen the PCI generation.
 * @param pcieBwGBs the PCIe bandwidth in GB/s.
 * @param bwNvlinkGBs the NVLink bandwidth in GB/s.
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
