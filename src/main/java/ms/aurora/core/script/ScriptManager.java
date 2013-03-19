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

    public ScriptManager(Session session) {
        this.session = session;
    }

    public void start(Script script) {
        script.setSession(session);
        Future<?> future = executorService.submit(script);
        futures.put(script, future);
    }

    public void pause() {
        for(Script script : futures.keySet()) {
            script.setState(ScriptState.PAUSED);
        }
    }

    public void resume() {
        for(Script script : futures.keySet()) {
            script.setState(ScriptState.RUNNING);
        }
    }

    public void stop() {
        for(Script script : futures.keySet()) {
            script.setState(ScriptState.STOP);
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public void shutdownNow() {
        executorService.shutdownNow();
    }
}
