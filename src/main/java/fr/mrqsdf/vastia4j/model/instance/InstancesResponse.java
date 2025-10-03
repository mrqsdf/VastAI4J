// src/main/java/fr/mrqsdf/vastia4j/model/instance/InstancesResponse.java
package fr.mrqsdf.vastia4j.model.instance;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Wrapper de la route GET /instances/ (liste)
 */
public record InstancesResponse(
        @SerializedName("instances") List<InstanceSummary> instances
) {
}
