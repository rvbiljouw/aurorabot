package ms.aurora.core.script;

import com.google.common.collect.Maps;
import ms.aurora.api.script.LoopScript;
import ms.aurora.core.Session;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ScriptManager {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<LoopScript, Future<?>> futures = Maps.newHashMap();
    private final Session session;

    public ScriptManager(Session session) {
        this.session = session;
    }

    public void start(LoopScript script) {
        script.setSession(session);

        Future<?> future = executorService.submit(script);
        futures.put(script, future);
    }

    public void stop(LoopScript script) {
        Future<?> future = futures.get(script);
        if (future != null) {
            future.cancel(true);
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public void shutdownNow() {
        executorService.shutdownNow();
    }
}
