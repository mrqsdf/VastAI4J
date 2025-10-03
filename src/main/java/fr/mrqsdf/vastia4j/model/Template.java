package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

/**
 * Simplified Vast.ai template model. Extend or trim fields as needed for your use case.
 */
public record Template(
        long id,
        String name,
        String image,
        @SerializedName("hash_id") String hashId,
        @SerializedName("description") String description,
        @SerializedName("owner") String ownerUsername,
        @SerializedName("owner_id") Long ownerId,
        @SerializedName("public") Boolean isPublic,
        @SerializedName("recommended") Boolean isRecommended,
        @SerializedName("created_at") Long createdAtEpochSec
) {
}
