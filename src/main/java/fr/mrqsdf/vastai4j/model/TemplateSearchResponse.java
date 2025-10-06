package fr.mrqsdf.vastai4j.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response model for {@code GET /templates/} which includes the success status,
 * the number of templates found, and the list of templates.
 *
 * @param success        indicates if the request was successful.
 * @param templatesFound the number of templates found.
 * @param templates      the list of templates returned by the API.
 */
public record TemplateSearchResponse(
        boolean success,
        @SerializedName("templates_found") int templatesFound,
        List<Template> templates
) {
}
