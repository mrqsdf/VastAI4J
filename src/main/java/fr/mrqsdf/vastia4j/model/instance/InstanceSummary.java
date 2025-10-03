// src/main/java/fr/mrqsdf/vastia4j/model/instance/InstanceSummary.java
package fr.mrqsdf.vastia4j.model.instance;

import com.google.gson.annotations.SerializedName;

/**
 * Summary representation returned by {@code GET /instances/}.
 * Gson tolerates missing fields, so feel free to adjust the projection when necessary.
 * @param id the unique identifier of the instance.
 * @param machineId the machine ID associated with the instance.
 * @param label the label of the instance.
 * @param curState the current state of the instance.
 * @param actualStatus the actual status of the instance.
 * @param intendedStatus the intended status of the instance.
 * @param  gpuName the name of the GPU used by the instance.
 * @param numGpus the number of GPUs allocated to the instance.
 * @param pricePerHourUSD the price per hour in USD.
 * @param dlperfPerDphTotal the DLPerf per total DPH.
 * @param dphBase the base DPH.
 * @param dphTotal the total DPH.
 * @param imageUuid the UUID of the image used by the instance.
 * @param templateId the template ID associated with the instance.
 * @param geoCountryCode the geolocation country code of the instance.
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
