package ms.aurora.warb0.monkfighter.action;

import ms.aurora.api.methods.GroundItems;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSGroundItem;
import ms.aurora.warb0.script.Action;

import java.util.Arrays;

/**
 * @author tobiewarburton
 */
public class PickupAction extends Action {
    private static int[] pickup = new int[]{

    };
    private static Predicate<RSGroundItem> predicate = new Predicate<RSGroundItem>() {
        @Override
        public boolean apply(RSGroundItem object) {
            return Arrays.asList(pickup).contains(object.getId());
        }
    };

    @Override
    public boolean activate() {
        return me().isIdle();
    }

    @Override
    public void execute() {
        RSGroundItem item = GroundItems.get(predicate);
        if (item != null) {
            boolean success = item.applyAction("Take");
            if (!success) {
                while (!me().isIdle()) {
                    sleep(50);
                }
                item.applyAction("Take"); // last attempt
            }
        }
    }
}
