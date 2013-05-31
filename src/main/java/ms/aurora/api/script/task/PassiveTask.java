package ms.aurora.api.script.task;

import static ms.aurora.api.Context.getSession;

/**
 * @author rvbiljouw
 */
public abstract class PassiveTask implements Task {
    protected TaskQueue queue;
    private Thread lastExecution;

    public abstract boolean canRun();

    /**
     * Main executor for the task.
     *
     * @return time until the task should be ran again or
     *         -1 if it shouldn't be ran again.
     */
    public abstract int execute();

    public void run() {
        if (lastExecution == null || !lastExecution.isAlive()) {
            lastExecution = new Thread(getSession().getThreadGroup(), body);
            lastExecution.setName(getClass().getSimpleName());
            lastExecution.start();
        }
    }

    public int getPriority() {
        return 0;
    }

    private Runnable body = new Runnable() {
        @Override
        public void run() {
            if (canRun()) {
                int sleep = execute();
                try {
                    if (sleep > 0) {
                        Thread.sleep(sleep);
                    } else {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void setQueue(TaskQueue queue) {
        this.queue = queue;
    }
}
