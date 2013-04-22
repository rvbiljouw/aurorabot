package ms.aurora.api.script.task;

import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptState;

import java.util.Comparator;
import java.util.PriorityQueue;

import static java.lang.Thread.currentThread;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author rvbiljouw
 */
public class TaskQueue extends PriorityQueue<Task> implements Runnable {
    private final PriorityQueue<PassiveTask> passive_internal =
            new PriorityQueue<PassiveTask>(16, taskComparator);
    private final Script owner;
    private final EventBus eventBus;

    /**
     * Constructor
     * @param owner The script that owns this task queue.
     */
    public TaskQueue(Script owner) {
        super(16, taskComparator);
        this.owner = owner;
        this.eventBus = owner.getEventBus();
    }

    /**
     * Execution
     */
    public void run() {
        // Initialize the passive processor.
        Thread passiveThread = new Thread(passiveProcessor);
        passiveThread.start();

        while(owner.getState() != ScriptState.STOP && !currentThread().isInterrupted()) {
            if(owner.getState() == ScriptState.PAUSED) {
                sleepNoException(100);
                continue;
            }

            Task currentTask = poll();
            if(currentTask != null) {
                currentTask.setQueue(this);
                currentTask.run();
            }
        }

        passiveThread.interrupt();
    }

    @Override
    public boolean add(Task task) {
        if(task instanceof PassiveTask) {
            return passive_internal.add((PassiveTask)task);
        }
        return super.add(task);
    }

    /**
     * Gets the owner this task queue
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
            while(owner.getState() != ScriptState.STOP && !currentThread().isInterrupted()) {
                if(owner.getState() == ScriptState.PAUSED) {
                    sleepNoException(100);
                    continue;
                }

                Task currentTask = passive_internal.peek(); // Peek, don't remove.
                if(currentTask != null) {
                    currentTask.run();
                }
            }
        }
    };

    public EventBus getEventBus() {
        return eventBus;
    }
}
