package ms.aurora.warb0.script;

import ms.aurora.api.script.Script;

/**
 * @author tobiewarburton
 */
public abstract class ActionScript extends Script {
    private int tickDelay;

    public ActionScript() {
        this(100);
    }

    public ActionScript(int tickDelay) {
        this.tickDelay = tickDelay;
    }

    public abstract Action[] actions();

    public void refresh() {

    }

    @Override
    public int tick() {
        for (Action action : actions()) {
            if (action.activate()) {
                action.execute();
            }
        }
        refresh();
        return tickDelay;
    }
}
