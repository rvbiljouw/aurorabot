package ms.aurora.api.script.task;

import ms.aurora.api.Context;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptState;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Thread.currentThread;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author rvbiljouw
 */
public class TaskQueue extends PriorityQueue<Task> implements Runnable {
    private final CopyOnWriteArrayList<PassiveTask> passive_internal =
            new CopyOnWriteArrayList<PassiveTask>();
    private final TaskQueue self;
    private final Script owner;
    private final EventBus eventBus;
    private Thread passiveThread;

    /**
     * Constructor
     *
     * @param owner The script that owns this task queue.
     */
    public TaskQueue(Script owner) {
        super(16, taskComparator);
        this.owner = owner;
        this.eventBus = Context.getEventBus();
        this.self = this;
    }

    /**
     * Execution
     */
    public void run() {
        // Initialize the passive processor.
        passiveThread = new Thread(passiveProcessor);
        passiveThread.start();

        while (owner.getState() != ScriptState.STOP && !currentThread().isInterrupted()) {
            if (owner.getState() == ScriptState.PAUSED) {
                sleepNoException(100);
                continue;
            }

            Task currentTask = poll();
            if (currentTask != null) {
                currentTask.setQueue(this);
                currentTask.run();
            }
            sleepNoException(300);
        }

        passiveThread.interrupt();
    }

    @Override
    public boolean add(Task task) {
        if (task instanceof PassiveTask) {
            return passive_internal.add((PassiveTask) task);
        }
        return super.add(task);
    }

    /**
     * Gets the owner this task queue
     *
     * @return owner
     */
    public Script getOwner() {
        return owner;
    }

    /**
     * Task comparator, for sorting tasks based on priority.
     */
    private static Comparator<Task> taskComparator = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.getPriority() - o2.getPriority();
        }
    };

    private Runnable passiveProcessor = new Runnable() {
        @Override
        public void run() {
            while (owner.getState() != ScriptState.STOP && !currentThread().isInterrupted()) {
                try {
                    if (owner.getState() == ScriptState.PAUSED) {
                        sleepNoException(100);
                        continue;
                    }

                    for (Task currentTask : passive_internal) {
                        if (currentTask != null) {
                            currentTask.setQueue(self);
                            currentTask.run();
                        }
                    }
                    sleepNoException(200, 500);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    };

    public EventBus getEventBus() {
        return eventBus;
    }

    public void destruct() {
        passiveThread.interrupt();
        passiveThread.stop();
    }
}
