package fr.mrqsdf.vastai4j.query;

import java.util.Objects;

/**
 * Immutable pair representing an order-by clause (field + direction).
 *
 * @param field     the field to order by.
 * @param direction the direction to order by.
 */
public record OrderBy(String field, Direction direction) {
    public OrderBy {
        Objects.requireNonNull(field, "field");
        Objects.requireNonNull(direction, "direction");
    }
}
