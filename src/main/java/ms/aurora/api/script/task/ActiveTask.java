package ms.aurora.api.script.task;

/**
 * An active task is a task that "blocks" the task queue.
 * Only one active task can run at a time, and they are ran
 * based on order of their priority.
 *
 * @author rvbiljouw
 */
public abstract class ActiveTask implements Task {
    protected final TaskQueue queue;

    public ActiveTask(TaskQueue queue) {
        this.queue = queue;
    }

    /**
     * Checks if the task is still runnable, or has been invalidated.
     *
     * @return task runnable
     */
    public abstract boolean canRun();

    /**
     * Main executor for the task
     *
     * @returns the time to sleep until we move
     * on through the task queue.
     */
    public abstract int execute();

    public void run() {
        if (canRun()) {
            int sleep = execute();
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The priority of the task
     * Override this to change the ordering
     * of tasks within the task queue
     * <p/>
     * Tasks have a priority of 0 by default.
     *
     * @return priority
     */
    public int getPriority() {
        return 0;
    }

}
