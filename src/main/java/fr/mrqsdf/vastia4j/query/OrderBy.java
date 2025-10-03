package fr.mrqsdf.vastia4j.query;

import java.util.Objects;

/**
 * Immutable pair representing an order-by clause (field + direction).
 */
public record OrderBy(String field, Direction direction) {
    public OrderBy {
        Objects.requireNonNull(field, "field");
        Objects.requireNonNull(direction, "direction");
    }
}
