package fr.mrqsdf.vastai4j.event.instance;

import fr.mrqsdf.vastai4j.event.AbstractEvent;
import fr.mrqsdf.vastai4j.model.instance.Ports;

import java.util.List;
import java.util.Map;

/** Déclenché quand une table de mapping de ports Docker apparaît (externe -> interne). */
public final class InstancePortsMappedEvent extends AbstractEvent {
    private final long instanceId;
    /** map internePort -> externePort */
    private final Map<String, List<Ports>> mapping;
    private final String ip;

    public InstancePortsMappedEvent(long instanceId, Map<String, List<Ports>> mapping, String ip) {
        this.instanceId = instanceId;
        this.mapping = mapping;
        this.ip = ip;
    }
    public long instanceId() { return instanceId; }
    public Map<String, List<Ports>> mapping() { return mapping; }
    public String publicIpOrHost() { return ip; }
}
