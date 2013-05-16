package ms.aurora.api.script;

import ms.aurora.api.Context;
import ms.aurora.api.script.task.EventBus;

/**
 * @author Rick
 */
public abstract class Script extends Context implements Runnable {
    private final EventBus eventBus = new EventBus();
    private ScriptState state = ScriptState.START;

    public abstract int tick();

    public ScriptState getState() {
        return this.state;
    }

    public void setState(ScriptState state) {
        this.state = state;
    }

    public void onPause() {

    }

    public void onResume() {

    }

    public void onStart() {

    }

    public void onFinish() {

    }

    public void init() {

    }

    public void cleanup() {

    }

    public final ScriptManifest getManifest() {
        return getClass().getAnnotation(ScriptManifest.class);
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
