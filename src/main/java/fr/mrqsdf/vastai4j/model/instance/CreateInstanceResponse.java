package fr.mrqsdf.vastai4j.model.instance;

import com.google.gson.annotations.SerializedName;

/**
 * Minimal response returned by {@code PUT /asks/{id}/} when creating an instance.
 * @param success indicates if the request was successful.
 * @param newContract the ID of the newly created contract (instance), if successful.
 */
public record CreateInstanceResponse(
        boolean success,
        @SerializedName("new_contract") Long newContract
) {
}
