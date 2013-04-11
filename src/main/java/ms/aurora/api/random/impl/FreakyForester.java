package ms.aurora.api.random.impl;

import ms.aurora.api.methods.*;
import ms.aurora.api.methods.filters.ItemFilters;
import ms.aurora.api.methods.filters.NpcFilters;
import ms.aurora.api.methods.filters.ObjectFilters;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.random.Random;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.*;

/**
 * @author tobiewarburton
 *         note i have commented out the deposit box bit because we didn't have
 *         the method implemented inside the Bank class and im not sure if it's the same
 *         in rs07. I can implement it later
 */
public class FreakyForester extends Random {
    private RSNPC forester;
    private static final int FORESTER_ID = 2458;
    private static final int SEARCH_INTERFACE_ID = 242;
    private static final int PORTAL_ID = 8972;
    private static final RSTile WALK_TO_TILE = new RSTile(2610, 4775);
    private boolean unequip = false;
    int phe = -1;

    boolean done = false;

    @Override
    public boolean activate() {
        forester = Npcs.get(NpcFilters.ID(FORESTER_ID));
        if (forester != null) {
            Utilities.sleepNoException(2000, 3000);
            if (forester != null) {
                RSObject portal = Objects.get(ObjectFilters.ID(PORTAL_ID));
                return portal != null;
            }
        }
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getState() {
        if (done)
            return 3;
        else if (Widgets.canContinue())
            return 1;
        else if (phe == -1)
            return 0;
        else if (Inventory.contains(6178))
            return 0;
        else if (phe != -1)
            return 2;
        else
            return 0;
    }

    @Override
    public int loop() {
        forester = Npcs.get(NpcFilters.ID(FORESTER_ID));
        if (forester == null)
            return -1;

        if (Players.getLocal().getAnimation() != -1)
            return Utilities.random(3000, 5000);
        else if (Players.getLocal().isMoving())
            return Utilities.random(200, 500);

        if (!done) {
            done = searchText(241, "Thank you") || Widgets.getWidget(242, 4).getText().contains("leave");
        }

        if (Inventory.contains(6179)) {
            phe = -1;
            Inventory.get(6179).applyAction("Drop");
            return Utilities.random(500, 900);
        }

//        if (bank.isDepositOpen() || (Inventory.getCount(false) == 28) && !Inventory.containsAll(6178)) {
//            if (bank.isDepositOpen() && bank.getBoxCount() == 28) {
//                interfaces.get(11).getComponent(17).getComponent(random(21, 27)).applyAction("Deposit");
//                Utilities.sleepNoException(1000, 1500);
//            } else if (bank.isDepositOpen()) {
//                bank.close();
//                return Utilities.random(1000, 1500);
//            }
//            final RSObject box = objects.getNearest(32931);
//            if ((!calc.tileOnScreen(box.getLocation())
//                    && (
//                    (calc.distanceTo(walking.getDestination())) < 8))
//                    || (calc.distanceTo(walking.getDestination()) > 40)
//                    ) {
//                walking.clickMap(box.getLocation());
//                Utilities.sleepNoException(1200, 1400);
//            }
//            if (box.applyAction("Deposit")) {
//                return Utilities.random(800, 1200);
//            }
//        }

        switch (getState()) {
            case 0: // Talk to forester
                if (Viewport.tileOnScreen(forester.getLocation())
                        && (Calculations.distance(Players.getLocal().getLocation(), forester.getLocation()) <= 5)) {
                    forester.applyAction("Talk");
                } else {
                    Walking.clickOnMap(forester.getLocation());
                }
                return Utilities.random(500, 800);
            case 1: // Talking
                if (searchText(SEARCH_INTERFACE_ID, "one-")) {
                    phe = 2459;
                } else if (searchText(SEARCH_INTERFACE_ID, "two-")) {
                    phe = 2460;
                } else if (searchText(SEARCH_INTERFACE_ID, "three-")) {
                    phe = 2461;
                }
                if (searchText(SEARCH_INTERFACE_ID, "four-")) {
                    phe = 2462;
                }
                if (myClickContinue())
                    return Utilities.random(500, 800);
                return Utilities.random(200, 500);
            case 2: // Kill pheasant
                if (phe == -1)
                    return Utilities.random(200, 500);
                final RSNPC Pheasant = Npcs.get(NpcFilters.ID(phe));
                final RSGroundItem tile = GroundItems.get(ItemFilters.ID(6178));
                if (tile != null) {
                    tile.applyAction("Take");
                    return Utilities.random(600, 900);
                } else if (Pheasant != null) {
                    if (Viewport.tileOnScreen(Pheasant.getLocation())) {
                        Pheasant.applyAction("Attack");
                        return Utilities.random(1000, 1500);
                    } else if (Calculations.distance(Players.getLocal().getLocation(), Pheasant.getLocation()) >= 5) {
                        Walking.clickOnMap(Pheasant.getLocation());
                    }
                } else
                    return Utilities.random(2000, 5000);
            case 3: // Get out
                Walking.clickOnMap(WALK_TO_TILE);
                final RSObject portal = Objects.get(ObjectFilters.ID(PORTAL_ID));

                if (portal == null) {
                    return Utilities.random(800, 1200);
                }

                if (portal.applyAction("Enter")) {
                    return Utilities.random(800, 1200);
                }
                return Utilities.random(200, 500);
        }
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean myClickContinue() {
        Utilities.sleepNoException(800, 1000);
        return Widgets.getWidget(243, 7).click(true)
                || Widgets.getWidget(241, 5).click(true)
                || Widgets.getWidget(242, 6).click(true)
                || Widgets.getWidget(244, 8).click(true)
                || Widgets.getWidget(64, 5).click(true);
    }

    public boolean searchText(final int interfac, final String text) {
        final RSWidgetGroup talkFace = Widgets.getWidgets(interfac);
        if (!talkFace.isValid()) {
            return false;
        }
        for (RSWidget child : talkFace.getWidgets()) {
            if (child.getText().contains(text)) {
                return true;
            }
        }
        return false;
    }
}
