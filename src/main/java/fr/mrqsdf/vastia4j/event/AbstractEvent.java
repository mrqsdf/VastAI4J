package fr.mrqsdf.vastia4j.event;

/** Event de base avec timestamp. */
public abstract class AbstractEvent implements Event {
    private final long ts = System.currentTimeMillis();
    @Override public long timestampMillis() { return ts; }
}
