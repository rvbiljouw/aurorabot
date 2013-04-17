package ms.aurora.api.script.task;

/**
 * @author rvbiljouw
 */
public interface Task extends Runnable {

    public int getPriority();

}
