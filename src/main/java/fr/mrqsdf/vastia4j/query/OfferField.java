package fr.mrqsdf.vastia4j.query;

/**
 * Enumerates the documented fields accepted by Vast.ai when filtering offers.
 * Reference: "Search offers" &rarr; "Available fields" in the official API documentation.
 */
public enum OfferField {
    BW_NVLINK("bw_nvlink"),
    COMPUTE_CAP("compute_cap"),
    CPU_CORES("cpu_cores"),
    CPU_CORES_EFFECTIVE("cpu_cores_effective"),
    CPU_RAM("cpu_ram"),
    CUDA_VERS("cuda_vers"),
    DATACENTER("datacenter"),
    DIRECT_PORT_COUNT("direct_port_count"),
    DISK_BW("disk_bw"),
    DISK_SPACE("disk_space"),
    DLPERF("dlperf"),
    DLPERF_USD("dlperf_usd"),
    DPH("dph"),
    DRIVER_VERSION("driver_version"),
    DURATION("duration"),
    EXTERNAL("external"),
    FLOPS_USD("flops_usd"),
    GEOLOCATION("geolocation"),
    GPU_MEM_BW("gpu_mem_bw"),
    GPU_NAME("gpu_name"),
    GPU_RAM("gpu_ram"),
    GPU_FRAC("gpu_frac"),
    GPU_DISPLAY_ACTIVE("gpu_display_active"),
    HAS_AVX("has_avx"),
    ID("id"),
    INET_DOWN("inet_down"),
    INET_DOWN_COST("inet_down_cost"),
    INET_UP("inet_up"),
    INET_UP_COST("inet_up_cost"),
    MACHINE_ID("machine_id"),
    MIN_BID("min_bid"),
    NUM_GPUS("num_gpus"),
    PCI_GEN("pci_gen"),
    PCIE_BW("pcie_bw"),
    RELIABILITY("reliability"),
    RENTABLE("rentable"),
    RENTED("rented"),
    STORAGE_COST("storage_cost"),
    STATIC_IP("static_ip"),
    TOTAL_FLOPS("total_flops"),
    UBUNTU_VERSION("ubuntu_version"),
    VERIFIED("verified");

    private final String json;

    OfferField(String json) {
        this.json = json;
    }

    public String json() {
        return json;
    }

    /**
     * Looks up the enum constant by its JSON key without throwing when unknown.
     */
    public static OfferField fromJsonOrNull(String key) {
        for (OfferField f : values()) if (f.json.equals(key)) return f;
        return null;
    }
}
