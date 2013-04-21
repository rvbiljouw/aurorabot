package ms.aurora.api.script.task;

/**
 * @author rvbiljouw
 */
public abstract class PassiveTask implements Task {
    protected final TaskQueue queue;
    private Thread lastExecution;

    public PassiveTask(TaskQueue queue) {
        this.queue = queue;
    }

    public abstract boolean canRun();

    /**
     * Main executor for the task.
     *
     * @return time until the task should be ran again or
     *         -1 if it shouldn't be ran again.
     */
    public abstract int execute();

    public void run() {
        if(lastExecution == null || !lastExecution.isAlive()) {
            lastExecution = new Thread(queue.getOwner().getSession().getThreadGroup(), body);
            lastExecution.start();
        }
    }

    public int getPriority() {
        return 0;
    }

    private Runnable body = new Runnable() {
        @Override
        public void run() {
            if(canRun()) {
                int sleep = execute();
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
