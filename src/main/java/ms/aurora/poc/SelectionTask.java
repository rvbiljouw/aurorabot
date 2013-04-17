package ms.aurora.poc;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Objects;
import ms.aurora.api.script.task.PassiveTask;
import ms.aurora.api.script.task.TaskQueue;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSObject;

/**
 * @author rvbiljouw
 */
public class SelectionTask extends PassiveTask {
    private final DeferredSelectionListener listener;

    public SelectionTask(TaskQueue queue) {
        super(queue);

        if(queue.getOwner() instanceof DeferredSelectionListener) {
            listener = (DeferredSelectionListener)queue.getOwner();
        } else {
            listener = null;
        }
    }

    @Override
    public boolean canRun() {
        return Context.isLoggedIn();
    }

    @Override
    public int execute() {
        if(listener != null) {
            RSObject object = Objects.get(listener.getPredicate());
            if(object != null) {
                listener.selectObject(object);
            }
        }
        return 200;
    }
}
