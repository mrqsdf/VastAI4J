package fr.mrqsdf.vastia4j.model.instance;

import com.google.gson.annotations.SerializedName;

/**
 * Minimal response returned by {@code PUT /asks/{id}/} when creating an instance.
 */
public record CreateInstanceResponse(
        boolean success,
        @SerializedName("new_contract") Long newContract
) {
}
