// src/main/java/fr/mrqsdf/vastia4j/model/instance/InstanceSummary.java
package fr.mrqsdf.vastia4j.model.instance;

import com.google.gson.annotations.SerializedName;

/**
 * Summary representation returned by {@code GET /instances/}.
 * Gson tolerates missing fields, so feel free to adjust the projection when necessary.
 */
public record InstanceSummary(
        Long id,
        @SerializedName("machine_id") Long machineId,
        String label,
        @SerializedName("cur_state") String curState,
        @SerializedName("actual_status") String actualStatus,
        @SerializedName("intended_status") String intendedStatus,
        @SerializedName("gpu_name") String gpuName,
        @SerializedName("num_gpus") Integer numGpus,
        @SerializedName("dph") Double pricePerHourUSD,
        @SerializedName("dlperf_per_dphtotal") Double dlperfPerDphTotal,
        @SerializedName("dph_base") Double dphBase,
        @SerializedName("dph_total") Double dphTotal,
        @SerializedName("image_uuid") String imageUuid,
        @SerializedName("image") String image,
        @SerializedName("template_id") Long templateId,
        @SerializedName("geolocation") String geoCountryCode
) {
}
