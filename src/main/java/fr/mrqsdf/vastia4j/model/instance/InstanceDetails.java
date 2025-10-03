package fr.mrqsdf.vastia4j.model.instance;

import com.google.gson.annotations.SerializedName;

/**
 * Modèle partiel pour GET /instances/{id}/ (ajoute d’autres champs au besoin).
 */
public record InstanceDetails(
        @SerializedName("instances") InstancePayload instances
) {
    public record InstancePayload(
            Long id,
            @SerializedName("actual_status") String actualStatus,
            @SerializedName("cur_state") String curState,
            @SerializedName("intended_status") String intendedStatus,
            @SerializedName("ssh_host") String sshHost,
            @SerializedName("ssh_port") Integer sshPort,
            @SerializedName("ssh_idx") String sshIdx,
            @SerializedName("template_id") Long templateId,
            @SerializedName("template_hash_id") String templateHashId,
            @SerializedName("image_uuid") String imageUuid,
            @SerializedName("image_runtype") String imageRuntype,
            String label
    ) {
    }
}
