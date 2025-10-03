package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response model for {@code POST /bundles/} which wraps the {@code offers} array.
 * @param offers the list of offers returned by the API.
 */
public record OfferListResponse(@SerializedName("offers") List<Offer> offers) {
}
