package ms.aurora.api.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Runtime.getRuntime;

/**
 * @author rvbiljouw
 */
public class Tasks {
    private final ExecutorService executor = Executors.newFixedThreadPool(
            getRuntime().availableProcessors());
    private final ExecutorService timedExecutor = Executors.newScheduledThreadPool(16);



}
