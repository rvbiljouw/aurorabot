package ms.aurora.api.script;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tobiewarburton
 * @author A_C/Cov
 */
public abstract class ActionScript extends Script {

    private ArrayList<Action> actions = new ArrayList<Action>();

    public void submit(Action action) {
        this.actions.add(action);
    }

    public Action[] getActions() {
        return actions.toArray(new Action[actions.size()]);
    }

    public void remove(Action action) {
        this.actions.remove(action);
    }

    public abstract void onStart();

    @Override
    public int tick() {
        for (Action action : actions) {
            if (action.activate()) {
                return action.execute();
            }
        }
        return 200;
    }

}
