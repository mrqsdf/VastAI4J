package fr.mrqsdf.vastai4j.event;

import java.lang.annotation.*;

/** Annotation façon Spigot pour marquer les handlers. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    EventPriority priority() default EventPriority.NORMAL;
    boolean ignoreCancelled() default true; // à la Spigot
}
