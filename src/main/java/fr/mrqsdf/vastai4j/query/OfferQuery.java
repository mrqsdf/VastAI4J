package fr.mrqsdf.vastai4j.query;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * Fluent builder for Vast.ai offer searches (usable with both {@code POST /bundles/} and
 * {@code PUT /search/asks/}).
 * String-based helpers are preserved while enum-based overloads are provided for convenience.
 */
public final class OfferQuery {

    private final Map<String, JsonObject> filters = new LinkedHashMap<>();
    private final List<OrderBy> order = new ArrayList<>();

    private String type = "on-demand";
    private Integer limit = null;
    private Double allocatedStorageGiB = 5.0;
    private boolean disableBundling = false;
    private boolean noDefault = false;

    /**
     * @return this instance for chaining.
     */
    public OfferQuery type(String type) {
        this.type = Objects.requireNonNull(type);
        return this;
    }

    /**
     * @return this instance for chaining.
     */
    public OfferQuery limit(Integer limit) {
        this.limit = limit;
        return this;
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery allocatedStorageGiB(Double gib) {
        this.allocatedStorageGiB = gib;
        return this;
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery disableBundling(boolean v) {
        this.disableBundling = v;
        return this;
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery noDefault(boolean v) {
        this.noDefault = v;
        return this;
    }

    // ---------- WHERE: string-based variants (existing API surface) ----------
    /**
     * @return this instance for chaining.
     */
    public OfferQuery where(String field, Op op, JsonElement value) {
        Objects.requireNonNull(field);
        Objects.requireNonNull(op);
        Objects.requireNonNull(value);
        JsonObject cond = filters.computeIfAbsent(field, k -> new JsonObject());
        cond.add(op.jsonKey(), value);
        return this;
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery where(String field, Op op, String value) {
        return where(field, op, new com.google.gson.JsonPrimitive(value));
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery where(String field, Op op, Number value) {
        return where(field, op, new com.google.gson.JsonPrimitive(value));
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery where(String field, Op op, boolean value) {
        return where(field, op, new com.google.gson.JsonPrimitive(value));
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery where(String field, Op op, Collection<?> values) {
        JsonArray arr = new JsonArray();
        for (Object o : values) {
            if (o instanceof Number n) arr.add(n);
            else if (o instanceof Boolean b) arr.add(b);
            else arr.add(String.valueOf(o));
        }
        return where(field, op, arr);
    }
    /**
     * @return this instance for chaining.
     */
    // ---------- WHERE: enum overloads ----------
    public OfferQuery where(OfferField field, Op op, JsonElement value) {
        return where(field.json(), op, value);
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery where(OfferField field, Op op, String value) {
        return where(field.json(), op, value);
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery where(OfferField field, Op op, Number value) {
        return where(field.json(), op, value);
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery where(OfferField field, Op op, boolean value) {
        return where(field.json(), op, value);
    }
    /**
     * @return this instance for chaining.
     */
    public OfferQuery where(OfferField field, Op op, Collection<?> values) {
        return where(field.json(), op, values);
    }

    // ---------- ORDER: string-based variant ----------
    /**
     * @return this instance for chaining.
     */
    public OfferQuery orderBy(String field, Direction dir) {
        order.add(new OrderBy(field, dir));
        return this;
    }

    // ---------- ORDER: enum overload ----------
    /**
     * @return this instance for chaining.
     */
    public OfferQuery orderBy(OrderField field, Direction dir) {
        return orderBy(field.json(), dir);
    }

    // ---------- Build payload ----------
    /**
     * Builds the JSON object representing the query filters, order, type, limit, and other options.
     *
     * @return the JSON object representing the query.
     */
    public JsonObject toQueryJson() {
        JsonObject q = new JsonObject();
        if (!noDefault) {
            q.add("verified", one("eq", true));
            q.add("external", one("eq", false));
            q.add("rentable", one("eq", true));
            q.add("rented", one("eq", false));
        }
        for (Map.Entry<String, JsonObject> e : filters.entrySet()) {
            JsonObject dest = q.has(e.getKey()) && q.get(e.getKey()).isJsonObject()
                    ? q.getAsJsonObject(e.getKey()) : new JsonObject();
            for (Map.Entry<String, JsonElement> kv : e.getValue().entrySet()) {
                dest.add(kv.getKey(), kv.getValue());
            }
            q.add(e.getKey(), dest);
        }
        if (!order.isEmpty()) {
            JsonArray arr = new JsonArray();
            for (OrderBy ob : order) {
                JsonArray pair = new JsonArray();
                pair.add(ob.field());
                pair.add(ob.direction().json());
                arr.add(pair);
            }
            q.add("order", arr);
        }
        q.addProperty("type", type);
        if (limit != null) q.addProperty("limit", limit);
        if (allocatedStorageGiB != null) q.addProperty("allocated_storage", allocatedStorageGiB);
        if (disableBundling) q.addProperty("disable_bundling", true);
        return q;
    }

    /**
     * Builds the full JSON payload for a {@code PUT /search/asks/} request,
     * including the query, selected columns, and other options.
     *
     * @return the JSON object representing the full search payload.
     */
    public com.google.gson.JsonObject toSearchAsksPayload() {
        com.google.gson.JsonObject root = new com.google.gson.JsonObject();
        com.google.gson.JsonArray select = new com.google.gson.JsonArray();
        select.add("*");
        root.add("select_cols", select);
        root.add("q", toQueryJson());
        return root;
    }

    private static JsonObject one(String key, boolean val) {
        JsonObject o = new JsonObject();
        o.addProperty(key, val);
        return o;
    }
}
