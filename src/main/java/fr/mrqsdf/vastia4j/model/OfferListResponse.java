package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/** Réponse de POST /bundles/ (contient un tableau "offers"). */
public record OfferListResponse(@SerializedName("offers") List<Offer> offers) {}
