package fr.mrqsdf.vastai4j.event.instance;

import fr.mrqsdf.vastai4j.event.AbstractEvent;

/** Déclenché quand l’état courant change (cur_state, actual_status). */
public final class InstanceStateChangeEvent extends AbstractEvent {
    private final long instanceId;
    private final String previousCurState;
    private final String newCurState;
    private final String previousActualStatus;
    private final String newActualStatus;

    public InstanceStateChangeEvent(long instanceId, String prevCur, String nowCur,
                                    String prevAct, String nowAct) {
        this.instanceId = instanceId;
        this.previousCurState = prevCur;
        this.newCurState = nowCur;
        this.previousActualStatus = prevAct;
        this.newActualStatus = nowAct;
    }

    public long instanceId() { return instanceId; }
    public String previousCurState() { return previousCurState; }
    public String newCurState() { return newCurState; }
    public String previousActualStatus() { return previousActualStatus; }
    public String newActualStatus() { return newActualStatus; }
}
