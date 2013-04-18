package ms.aurora.warb0.autofighter.action;

import ms.aurora.api.methods.GroundItems;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSGroundItem;
import ms.aurora.warb0.autofighter.Constants;
import ms.aurora.warb0.script.Action;

import java.util.Arrays;

/**
 * @author tobiewarburton
 */
public class PickupAction extends Action {
    private static Predicate<RSGroundItem> predicate = new Predicate<RSGroundItem>() {
        @Override
        public boolean apply(RSGroundItem object) {
            return Arrays.asList(Constants.PICKUP_ID).contains(object.getId());
        }
    };

    @Override
    public boolean activate() {
        return me().isIdle() && Constants.PICKUP;
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
