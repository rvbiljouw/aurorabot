package ms.aurora.core.script;

import com.google.common.collect.Maps;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptState;
import ms.aurora.core.Session;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class ScriptManager {
    private final ExecutorService executorService = Executors.newFixedThreadPool(16);
    private final Map<Script, Future<?>> futures = Maps.newHashMap();
    private final Session session;
    private State state = State.STOPPED;

    public ScriptManager(Session session) {
        this.session = session;
    }

    public void start(Script script) {
        script.setSession(session);
        Future<?> future = executorService.submit(script);
        futures.put(script, future);
        state = State.RUNNING;
    }

    public void pause() {
        for(Script script : futures.keySet()) {
            script.setState(ScriptState.PAUSED);
        }
        state = State.PAUSED;
    }

    public void resume() {
        for(Script script : futures.keySet()) {
            script.setState(ScriptState.RUNNING);
        }
        state = State.RUNNING;
    }

    public void stop() {
        for(Script script : futures.keySet()) {
            script.setState(ScriptState.STOP);
            futures.remove(script);
        }
        state = State.STOPPED;
    }

    public void shutdown() {
        executorService.shutdown();
        state = State.STOPPED;
    }

    public void shutdownNow() {
        executorService.shutdownNow();
        state = State.STOPPED;
    }

    public State getState() {
        return state;
    }

    public static enum State {
        STOPPED, RUNNING, PAUSED
    }
}
