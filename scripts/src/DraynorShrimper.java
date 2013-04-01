import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Walking;
import ms.aurora.api.methods.tabs.Bank;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

import static ms.aurora.api.methods.filters.NpcFilters.ID;

/**
 * Date: 26/03/13
 * Time: 16:23
 *
 * @author A_C/Cov
 */
@ScriptManifest(name = "Draynor Shrimper", author = "A_C", version = 1.0, shortDescription = "Catches and banks shrimps in draynor", category = "Fishing")
public class DraynorShrimper extends Script implements PaintListener {

    private static final RSTile FISHING_RSTILE = new RSTile(3087, 3229), BANK_RSTILE = new RSTile(3094, 3243);
    private static final RSTile[] FISHING_SPOT_PATH = new RSTile[] { new RSTile(3094,3243), new RSTile(3091,3247),
            new RSTile(3087,3244), new RSTile(3087,3239), new RSTile(3087,3234), new RSTile(3087,3229) };
    private static final RSTile[] BANK_SPOT_PATH = new RSTile[] { new RSTile(3087,3229), new RSTile(3090,3233),
            new RSTile(3088,3238), new RSTile(3087,3243), new RSTile(3090,3247), new RSTile(3094,3243) };

    @Override
    public int tick() {

        if (nearTile(FISHING_RSTILE)) {
            if (Inventory.isFull()) {
                walkToTile(BANK_RSTILE, BANK_SPOT_PATH);
                return Utilities.random(500, 1000);
            } else {
                if (Players.getLocal().getAnimation() == -1 && !Players.getLocal().isMoving()) {
                    RSNPC spot = Npcs.get(ID(327));
                    if (spot != null) {
                        if (spot.applyAction("Net")) {
                            return Utilities.random(500, 1000);
                        }
                    }
                }
            }
        } else if (nearTile(BANK_RSTILE)) {
            if (!Inventory.containsAny(317, 321)) {
                walkToTile(FISHING_RSTILE, FISHING_SPOT_PATH);
                return Utilities.random(500, 1000);
            } else {
                if (!Bank.isOpen()) {
                    if (Bank.open()) {
                        return Utilities.random(500, 1000);
                    }
                } else {
                    Inventory.InventoryItem item = Inventory.get(317);
                    Inventory.InventoryItem item2 = Inventory.get(321);
                    if (item != null) {
                        if (item.applyAction("Store All")) {
                            return Utilities.random(500, 1000);
                        }
                    }

                    if(item2 != null) {
                        if (item2.applyAction("Store All")) {
                            return Utilities.random(500, 1000);
                        }
                    }
                }
            }
        }
        return 0;
    }

    private boolean nearTile(RSTile tile) {
        return Calculations.distance(Players.getLocal().getLocation(), tile) < 5;
    }

    private void walkToTile(RSTile target, RSTile[] path) {
        while (!nearTile(target)) {
            if (!Players.getLocal().isMoving()) {
                if (!Walking.createPath(path).step()) {
                    Utilities.sleepNoException(500, 1000);
                }
            }
        }
    }

    @Override
    public void onRepaint(Graphics graphics) {
        graphics.drawString("Bank Open: " + Bank.isOpen(), 10, 40);
        //graphics.drawString("Bank Pane Length: " + widgets.getWidgets(12).getWidgets().length, 10, 52);
    }
}
