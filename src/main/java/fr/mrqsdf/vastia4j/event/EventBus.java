package fr.mrqsdf.vastia4j.event;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bus d’events minimaliste:
 * - register(listener) scanne @EventHandler sur des méthodes (un seul param Event)
 * - unregister(listener)
 * - call(event) appelle les handlers par priorité (LOWEST..MONITOR)
 */
public final class EventBus {

    private static final class Handler {
        final Object listener;
        final Method method;
        final EventPriority priority;
        final boolean ignoreCancelled;

        Handler(Object l, Method m, EventPriority p, boolean ig) {
            this.listener = l; this.method = m; this.priority = p; this.ignoreCancelled = ig;
            this.method.setAccessible(true);
        }
    }

    // Map: EventClass -> Handlers (toutes priorités confondues)
    private final Map<Class<?>, List<Handler>> handlers = new ConcurrentHashMap<>();

    public void register(Listener listener) {
        Objects.requireNonNull(listener, "listener");
        for (Method m : listener.getClass().getDeclaredMethods()) {
            EventHandler ann = m.getAnnotation(EventHandler.class);
            if (ann == null) continue;
            Class<?>[] params = m.getParameterTypes();
            if (params.length != 1 || !Event.class.isAssignableFrom(params[0])) {
                throw new IllegalArgumentException("@" + EventHandler.class.getSimpleName() +
                        " on " + m + " must have exactly one Event parameter");
            }
            Class<?> eventType = params[0];
            handlers.computeIfAbsent(eventType, k -> new ArrayList<>())
                    .add(new Handler(listener, m, ann.priority(), ann.ignoreCancelled()));
        }
        // ordre d’exécution = priorités LOWEST..HIGHEST puis MONITOR (comme Spigot) :contentReference[oaicite:1]{index=1}
        for (List<Handler> list : handlers.values()) {
            list.sort(Comparator.comparing(h -> h.priority));
        }
    }

    public void unregister(Listener listener) {
        handlers.values().forEach(list -> list.removeIf(h -> h.listener == listener));
    }

    public void call(Event event) {
        if (event == null) return;
        // Appel exact + superclasses/interfaces : on parcourt toutes les clés compatibles
        for (Map.Entry<Class<?>, List<Handler>> e : handlers.entrySet()) {
            if (!e.getKey().isAssignableFrom(event.getClass())) continue;
            for (Handler h : e.getValue()) {
                if (event instanceof Cancellable c && c.isCancelled() && h.ignoreCancelled) continue;
                invoke(h, event);
            }
        }
    }

    private static void invoke(Handler h, Event event) {
        try {
            // Appel direct par reflection (simple et suffisant ici)
            h.method.invoke(h.listener, event);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
