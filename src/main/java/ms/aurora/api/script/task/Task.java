package ms.aurora.api.script.task;

/**
 * @author rvbiljouw
 */
public interface Task extends Runnable {

    public boolean canRun();

    public int getPriority();

    public void setQueue(TaskQueue queue);

}
