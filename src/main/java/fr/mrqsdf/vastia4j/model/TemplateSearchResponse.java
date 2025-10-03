package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record TemplateSearchResponse(
        boolean success,
        @SerializedName("templates_found") int templatesFound,
        List<Template> templates
) {
}
