package ms.aurora.poc;

import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.script.task.ActiveTask;
import ms.aurora.api.script.task.TaskQueue;

/**
 * @author rvbiljouw
 */
public class DroppingTask extends ActiveTask {

    public DroppingTask(TaskQueue queue) {
        super(queue);
    }

    @Override
    public boolean canRun() {
        return Inventory.isFull();
    }

    @Override
    public int execute() {
        Inventory.dropAll(34133); // TODO: wotevz
        return 100;
    }

}
