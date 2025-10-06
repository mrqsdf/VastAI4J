package fr.mrqsdf.vastai4j.monitor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.mrqsdf.vastai4j.VastAI;
import fr.mrqsdf.vastai4j.event.EventBus;
import fr.mrqsdf.vastai4j.event.instance.InstancePortsMappedEvent;
import fr.mrqsdf.vastai4j.event.instance.InstanceSshReadyEvent;
import fr.mrqsdf.vastai4j.event.instance.InstanceStateChangeEvent;
import fr.mrqsdf.vastai4j.model.instance.InstanceDetails;
import fr.mrqsdf.vastai4j.service.InstanceService;

import java.util.*;
import java.util.concurrent.*;

/**
 * Surveille un ou plusieurs instanceId et émet des events quand:
 *  - l’état change (cur_state / actual_status)
 *  - SSH devient disponible (ssh_host+ssh_port)
 *  - une table de ports Docker (\"ports\" objet) apparaît
 */
public final class InstanceMonitor implements AutoCloseable {

    private final InstanceService instances;
    private final EventBus bus;
    private final ScheduledExecutorService ses;

    /** État connu pour un id. */
    private static final class Snapshot {
        final String curState;
        final String actualStatus;
        final boolean sshReady;
        final boolean portsMapped;

        Snapshot(String cur, String act, boolean ssh, boolean pm) {
            this.curState = cur; this.actualStatus = act; this.sshReady = ssh; this.portsMapped = pm;
        }
    }

    private final Map<Long, Snapshot> last = new ConcurrentHashMap<>();
    private final Map<Long, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    public InstanceMonitor(VastAI vast, EventBus bus) {
        this.instances = vast.instances();
        this.bus = Objects.requireNonNull(bus, "bus");
        this.ses = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r, "vast-instance-monitor");
            t.setDaemon(true);
            return t;
        });
    }

    /** Commence à surveiller un instanceId à intervalle fixe. */
    public void watch(long instanceId, long period, TimeUnit unit) {
        stop(instanceId);
        ScheduledFuture<?> f = ses.scheduleWithFixedDelay(() -> tick(instanceId),
                0, Math.max(250, unit.toMillis(period)), TimeUnit.MILLISECONDS);
        tasks.put(instanceId, f);
    }

    /** Arrête la surveillance d’un id. */
    public void stop(long instanceId) {
        ScheduledFuture<?> f = tasks.remove(instanceId);
        if (f != null) f.cancel(true);
        last.remove(instanceId);
    }

    /** Arrête tout. */
    @Override public void close() {
        tasks.values().forEach(f -> f.cancel(true));
        tasks.clear();
        ses.shutdownNow();
    }

    private void tick(long id) {
        try {
            InstanceDetails det = instances.show(id);
            InstanceDetails.InstancePayload p = det.instances();

            String cur = nz(p.curState());
            String act = nz(p.actualStatus());
            boolean ssh = p.sshHost() != null && p.sshPort() != null && p.sshPort() > 0;
            boolean portsMapped = hasDockerPortsObject(p);

            Snapshot prev = last.get(id);
            if (prev == null) {
                last.put(id, new Snapshot(cur, act, ssh, portsMapped));
                // première observation: si déjà ssh dispo / mapping présent, on peut émettre les events « ready »
                if (ssh) bus.call(new InstanceSshReadyEvent(id, p.sshHost(), p.sshPort()));
                if (portsMapped) bus.call(new InstancePortsMappedEvent(id, p.ports(), getIpOrHost(p)));
                return;
            }

            // Changement d’état?
            if (!Objects.equals(prev.curState, cur) || !Objects.equals(prev.actualStatus, act)) {
                bus.call(new InstanceStateChangeEvent(id, prev.curState, cur, prev.actualStatus, act));
            }

            // SSH vient d’apparaître ?
            if (!prev.sshReady && ssh) {
                bus.call(new InstanceSshReadyEvent(id, p.sshHost(), p.sshPort()));
            }

            // Mapping de ports Docker vient d’apparaître ?
            if (!prev.portsMapped && portsMapped) {
                bus.call(new InstancePortsMappedEvent(id, p.ports(), getIpOrHost(p)));
            }

            last.put(id, new Snapshot(cur, act, ssh, portsMapped));
        } catch (Throwable t) {
            // on ne casse pas le scheduler
            t.printStackTrace();
        }
    }

    // ---- helpers ----

    private static boolean hasDockerPortsObject(InstanceDetails.InstancePayload p) {
        // Ton modèle peut typer "ports" comme JsonElement ou Map-like.
        // Ici, on tente de récupérer via reflection le getter "ports" (record).
        try {
            var m = p.getClass().getDeclaredMethod("ports");
            Object v = m.invoke(p);
            if (v == null) return false;
            if (v instanceof JsonObject) return true;         // objet {"22/tcp":[...]}
            if (v instanceof Map<?,?>)   return true;
            // liste d’entiers => pas un mapping externe->interne
            return false;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer,Integer> extractPortsMap(InstanceDetails.InstancePayload p) {
        // support "ports" sous forme JsonObject Docker
        try {
            var m = p.getClass().getDeclaredMethod("ports");
            Object v = m.invoke(p);
            if (v instanceof JsonObject obj) {
                Map<Integer,Integer> out = new LinkedHashMap<>();
                for (String key : obj.keySet()) { // key ex: "22/tcp"
                    int internal = parseInternal(key);
                    JsonElement arrEl = obj.get(key);
                    if (arrEl != null && arrEl.isJsonArray() && arrEl.getAsJsonArray().size() > 0) {
                        JsonObject first = arrEl.getAsJsonArray().get(0).getAsJsonObject();
                        if (first.has("HostPort")) {
                            try {
                                int external = Integer.parseInt(first.get("HostPort").getAsString());
                                out.put(internal, external);
                            } catch (NumberFormatException ignore) {}
                        }
                    }
                }
                return out;
            } else if (v instanceof Map<?,?> map) {
                // Map<String,List<Map<String,String>>> comme renvoi Jackson-like
                Map<Integer,Integer> out = new LinkedHashMap<>();
                for (Map.Entry<?,?> e : map.entrySet()) {
                    String key = String.valueOf(e.getKey()); // "22/tcp"
                    int internal = parseInternal(key);
                    Object list = e.getValue();
                    if (list instanceof List<?> l && !l.isEmpty()) {
                        Object first = l.get(0);
                        if (first instanceof Map<?,?> m1 && m1.containsKey("HostPort")) {
                            try {
                                int external = Integer.parseInt(String.valueOf(m1.get("HostPort")));
                                out.put(internal, external);
                            } catch (NumberFormatException ignore) {}
                        }
                    }
                }
                return out;
            }
        } catch (ReflectiveOperationException ignore) { /* no-op */ }
        return Map.of();
    }

    private static int parseInternal(String key) {
        // "22/tcp" -> 22
        int slash = key.indexOf('/');
        String n = (slash > 0) ? key.substring(0, slash) : key;
        try { return Integer.parseInt(n); } catch (NumberFormatException e) { return -1; }
    }

    private static String getIpOrHost(InstanceDetails.InstancePayload p) {
        String ip = p.publicIpaddr();
        if (ip != null && !ip.isBlank()) return ip;
        return p.sshHost();
    }

    private static String nz(String s) { return s == null ? "" : s; }
}
