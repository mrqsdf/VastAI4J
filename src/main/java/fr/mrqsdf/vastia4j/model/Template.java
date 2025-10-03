package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

/**
 * Simplified Vast.ai template model. Extend or trim fields as needed for your use case.
 *
 * @param id                the unique identifier of the template.
 * @param name              the name of the template.
 * @param image             the Docker image associated with the template.
 * @param hashId            the hash ID of the template.
 * @param description       a brief description of the template.
 * @param ownerUsername     the username of the template's owner.
 * @param ownerId           the ID of the template's owner.
 * @param isPublic          indicates if the template is public.
 * @param isRecommended     indicates if the template is recommended by Vast.ai.
 * @param createdAtEpochSec the creation timestamp in epoch seconds.
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
