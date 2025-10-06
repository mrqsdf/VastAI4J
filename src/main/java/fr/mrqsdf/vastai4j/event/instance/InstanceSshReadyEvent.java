package fr.mrqsdf.vastai4j.event.instance;

import fr.mrqsdf.vastai4j.event.AbstractEvent;

/** Déclenché quand SSH devient disponible (ssh_host + ssh_port valides). */
public final class InstanceSshReadyEvent extends AbstractEvent {
    private final long instanceId;
    private final String sshHost;
    private final int sshPort;

    public InstanceSshReadyEvent(long instanceId, String sshHost, int sshPort) {
        this.instanceId = instanceId;
        this.sshHost = sshHost;
        this.sshPort = sshPort;
    }
    public long instanceId() { return instanceId; }
    public String sshHost() { return sshHost; }
    public int sshPort() { return sshPort; }
}
