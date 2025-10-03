package fr.mrqsdf.vastia4j.model.instance;

import com.google.gson.annotations.SerializedName;

/**
 * Réponse minimale de PUT /asks/{id}/
 */
public record CreateInstanceResponse(
        boolean success,
        @SerializedName("new_contract") Long newContract
) {
}
