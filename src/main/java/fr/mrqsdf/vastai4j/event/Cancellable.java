package fr.mrqsdf.vastai4j.event;

/** Un event annulable (si un jour tu veux intercepter une action). */
public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
