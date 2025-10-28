package fr.mrqsdf.vastai4j.event.instance;

import fr.mrqsdf.vastai4j.event.AbstractEvent;
import fr.mrqsdf.vastai4j.monitor.InstanceMonitor;

/** Déclenché quand SSH devient disponible (ssh_host + ssh_port valides). */
public final class InstanceSshReadyEvent extends AbstractEvent {
    private final long instanceId;
    private final String sshHost;
    private final int sshPort;
    private final InstanceMonitor monitor;

    public InstanceSshReadyEvent(long instanceId, String sshHost, int sshPort, InstanceMonitor monitor) {
        this.instanceId = instanceId;
        this.sshHost = sshHost;
        this.sshPort = sshPort;
        this.monitor = monitor;
    }
    public long instanceId() { return instanceId; }
    public String sshHost() { return sshHost; }
    public int sshPort() { return sshPort; }

    public InstanceMonitor monitor() { return monitor; }
}
