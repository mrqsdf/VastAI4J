package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response model for {@code POST /bundles/} which wraps the {@code offers} array.
 */
public record OfferListResponse(@SerializedName("offers") List<Offer> offers) {
}
