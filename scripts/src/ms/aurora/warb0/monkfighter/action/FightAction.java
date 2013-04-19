package ms.aurora.warb0.monkfighter.action;

import ms.aurora.api.methods.Npcs;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.warb0.script.Action;

import java.util.Arrays;

import static ms.aurora.api.util.Utilities.random;

/**
 * @author tobiewarburton
 */
public class FightAction extends Action {
    private Predicate<RSNPC> predicate = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return object.getName().equals("Monk") && !object.isInCombat();
        }
    };

    @Override
    public boolean activate() {
        boolean idle = me().isIdle();
        sleep(100);
        return me().isIdle() && idle;
    }

    @Override
    public void execute() {
        RSNPC monster = Npcs.get(predicate);
        if (monster != null) {
            boolean success = monster.applyAction("Attack");
            if (!success) {
                while (!me().isIdle()) {
                    sleep(50);
                }
                monster.applyAction("Attack"); // last attempt
                sleep(random(100, 200));
            }
        }
    }
}
