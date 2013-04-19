package ms.aurora.warb0.monkfighter.action;

import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.methods.tabs.Options;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.warb0.script.Action;

/**
 * @author tobiewarburton
 */
public class HealAction extends Action {
    private static Predicate<RSNPC> predicate = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return object.getName().equals("Abbot Langley");
        }
    };

    @Override
    public boolean activate() {
        return (me().getCurrentHealth() < 15 && me().isIdle()) || me().getCurrentHealth() < 10;
    }

    @Override
    public void execute() {
        Options.setRunning(true);
        RSNPC monk = Npcs.get(predicate);
        if (monk != null) {
            boolean success = monk.applyAction("Talk-to");
            if (!success) {
                while (!me().isIdle()) {
                    sleep(50);
                }
                monk.applyAction("Talk-to"); // last attempt
            }
        }
        handleContinue();
        handleHeal();
        for (int i = 0; i < 5; i++) {
            handleHeal();
        }
    }

    private void handleHeal() {
        RSWidget widget = Widgets.getWidget(230, 1);
        if (widget != null) {
            widget.click(true);
            sleep(50);
        }
    }

    private void handleContinue() {
        RSWidget widget = Widgets.getWidget(241, 3);
        if (widget != null) {
            widget.click(true);
            sleep(50);
        }
    }
}
