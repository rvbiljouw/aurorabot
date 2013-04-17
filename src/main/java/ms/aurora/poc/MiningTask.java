package ms.aurora.poc;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.script.task.ActiveTask;
import ms.aurora.api.script.task.TaskQueue;
import ms.aurora.api.wrappers.RSObject;

import static ms.aurora.api.Context.isLoggedIn;

/**
 * @author rvbiljouw
 */
public class MiningTask extends ActiveTask {
    private final DeferredSelectionListener listener;

    public MiningTask(TaskQueue queue) {
        super(queue);

        if(queue.getOwner() instanceof DeferredSelectionListener) {
            listener = (DeferredSelectionListener)queue.getOwner();
        } else {
            listener = null;
        }
    }

    @Override
    public boolean canRun() {
        return isLoggedIn() && Players.getLocal().isIdle()
                && Inventory.getCount() < 28;
    }

    @Override
    public int execute() {
        if(listener.getSelectedObject() != null) {
            RSObject object = listener.getSelectedObject();
            object.applyAction("Mine");
        }
        return 250;
    }
}
