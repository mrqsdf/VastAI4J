package fr.mrqsdf.vastia4j.query;

import java.util.Objects;

/**
 * Élément de tri : champ + direction.
 */
public record OrderBy(String field, Direction direction) {
    public OrderBy {
        Objects.requireNonNull(field, "field");
        Objects.requireNonNull(direction, "direction");
    }
}
