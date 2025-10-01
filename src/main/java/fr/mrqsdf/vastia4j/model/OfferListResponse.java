package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Response wrapper for Vast.ai offer listings.
 */
public class OfferListResponse {

    @SerializedName("offers")
    private List<Offer> offers;

    @SerializedName("total")
    private int total;

    @SerializedName("count")
    private int count;

    @SerializedName("limit")
    private int limit;

    @SerializedName("offset")
    private int offset;

    public List<Offer> getOffers() {
        return offers == null ? Collections.emptyList() : Collections.unmodifiableList(offers);
    }

    public int getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
}
