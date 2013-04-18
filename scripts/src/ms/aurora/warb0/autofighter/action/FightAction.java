package ms.aurora.warb0.autofighter.action;

import ms.aurora.api.methods.Npcs;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.warb0.autofighter.Constants;
import ms.aurora.warb0.script.Action;

import java.util.Arrays;

/**
 * @author tobiewarburton
 */
public class FightAction extends Action {
    private Predicate<RSNPC> predicate = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return Arrays.asList(Constants.MONSTER_ID).contains(object.getId())
                    || Arrays.asList(Constants.MONSTER_NAME).contains(object.getName());
        }
    };

    @Override
    public boolean activate() {
        return me().isIdle();
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
            }
        }
    }
}
