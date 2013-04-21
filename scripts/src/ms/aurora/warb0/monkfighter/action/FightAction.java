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
        return idle();
    }

    @Override
    public void execute() {
        RSNPC monster = Npcs.get(predicate);
        if (monster != null) {
            boolean success = monster.applyAction("Attack");
            if (!success) {
                while (!idle()) {
                    sleep(50);
                }
                monster.applyAction("Attack"); // last attempt
                while (me().isMoving()) {
                    sleep(random(100, 200));
                }
                sleep(500);
            }
        }
    }
}
