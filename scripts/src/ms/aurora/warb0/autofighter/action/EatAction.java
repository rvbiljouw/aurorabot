package ms.aurora.warb0.autofighter.action;

import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.warb0.autofighter.Constants;
import ms.aurora.warb0.script.Action;

/**
 * @author tobiewarburton
 */
public class EatAction extends Action {
    @Override
    public boolean activate() {
        int healthPercent = (100 / me().getMaxHealth()) * me().getCurrentHealth();
        return healthPercent < 30;
    }

    @Override
    public void execute() {
        Inventory.InventoryItem food = Inventory.get(Constants.FOOD_ID);
        if (food != null) {
            food.applyAction("Eat");
            sleep(200);
        } else {
            info("No food has been found and health is below 30%, attempting to teleport.");
            Inventory.InventoryItem teleport = Inventory.get(Constants.TELEPORT_ID);
            if (teleport != null) {
                teleport.applyAction("Break");
                sleep(5000);
            } else {
                error("Hopefully you're wearing a ring of life else you're probably going to die :(");
            }
        }
    }
}
