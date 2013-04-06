package ms.aurora.api.random.impl;

import ms.aurora.api.methods.*;
import ms.aurora.api.methods.filters.NpcFilters;
import ms.aurora.api.methods.filters.ObjectFilters;
import ms.aurora.api.random.Random;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;

import java.awt.*;

/**
 * @author tobiewarburton
 */
//TODO all
public class BeehiveSolver extends Random {
    private RSNPC beehiveKeeper;
    private static final int BEEHIVE_KEEPER_ID = 8649;
    private static final int[] DEST_INTERFACE_IDS = {16, 17, 18, 19};
    private static final int ID_DOWN = 16034;
    private static final int ID_MIDDOWN = 16022;
    private static final int ID_MIDUP = 16025;
    private static final int ID_TOP = 16036;
    private static final int[] BEEHIVE_ARRAYS = {ID_TOP, ID_MIDUP, ID_MIDDOWN, ID_DOWN};
    private static final String[] MODEL_NAMES = {"Top", "Middle Up", "Middle Down", "Down"};
    boolean solved;
    private static final int[] START_INTERFACE_IDS = {12, 13, 14, 15};
    private static final int INTERFACE_BEEHIVE_WINDOW = 420;
    private static final int BUILD_BEEHIVE = 40;
    private static final int CLOSE_WINDOW = 38;

    @Override
    public boolean activate() {
        return (Npcs.get(NpcFilters.ID(BEEHIVE_KEEPER_ID)) != null
                && Objects.get(ObjectFilters.ID(16168)) != null);
    }

    @Override
    public int loop() {
        beehiveKeeper = Npcs.get(NpcFilters.ID(BEEHIVE_KEEPER_ID));
        if (beehiveKeeper == null) {
            //log.severe("Beekeeper Random Finished Succesfully");
            return -1;
        }

        if (myClickContinue()) {
            return 200;
        }

        if (Widgets.getWidget(236, 2).click(true)) {
            return Utilities.random(800, 1200);
        }

        if (getBeehiveInterface().isValid()) {
            for (int i = 1; i < 5; i++) {
                int id = returnIdAtSlot(i);
                dragInterfaces(getBeehiveInterface().getWidgets()[START_INTERFACE_IDS[i - 1]], getBeehiveInterface().getWidgets()[returnDragTo(id)]);
            }
            Utilities.random(1000, 2000);
            //Wait is necessary for delay in the change of a setting.
            if (Settings.getSetting(805) == 109907968) {
                solved = true;
            } else {
                closeWindow();
                return Utilities.random(500, 1000);
            }
            if (solved && Widgets.getWidget(INTERFACE_BEEHIVE_WINDOW, BUILD_BEEHIVE).click(true)) {
                return Utilities.random(900, 1600);
            }
        }
        if (Players.getLocal().getInteracting() == null) {
            RSNPC npc = Npcs.get(NpcFilters.ID(BEEHIVE_KEEPER_ID));
            if (npc != null) {
                npc.applyAction("Talk-to");
            }
        }

        return Utilities.random(500, 1000);
    }

    public boolean dragInterfaces(RSWidget child1, RSWidget child2) {
        Point start = returnMidInterface(child1);
        Point finish = returnMidInterface(child2);
        input.getMouse().releaseMouse(start.x, start.y, true);
        input.getMouse().releaseMouse(finish.x, finish.y, true);
        return true;
    }

    public int returnDragTo(final int Model) {
        switch (Model) {
            case 16036:
                return DEST_INTERFACE_IDS[0];
            case 16025:
                return DEST_INTERFACE_IDS[1];
            case 16022:
                return DEST_INTERFACE_IDS[2];
            case 16034:
                return DEST_INTERFACE_IDS[3];
            default:
                return -1;
        }
    }

    public int returnIdAtSlot(final int slot) {
        if ((slot < 1) || (slot > 4)) {
            // invalid slot
            closeWindow();
        }

        int modelId = getBeehiveInterface().getWidgets()[returnSlotId(slot)].getModelId();

        if (modelId == -1) {
            closeWindow();
        }

        for (int i = 0; i < BEEHIVE_ARRAYS.length; i++) {
            if (modelId == BEEHIVE_ARRAYS[i]) {
                return modelId;
            }
        }

        return -1;
    }

    private void closeWindow() {
        RSWidget widget = Widgets.getWidget(INTERFACE_BEEHIVE_WINDOW, CLOSE_WINDOW);
        if (widget != null) {
            widget.click(true);
        }
    }

    public RSWidgetGroup getBeehiveInterface() {
        return Widgets.getWidgets(420);
    }

    public Point returnMidInterface(RSWidget child) {
        Point point = new Point(-1, -1);
        Rectangle rect = child.getArea();
        if (rect != null) {
            point = new Point((int) rect.getCenterX(), (int) rect.getCenterY());
        }
        return point;
    }

    public int returnSlotId(final int slot) {
        switch (slot) {
            case 1:
                return 25;
            case 2:
                return 22;
            case 3:
                return 23;
            case 4:
                return 21;
            default:
                RSWidget widget = Widgets.getWidget(INTERFACE_BEEHIVE_WINDOW, CLOSE_WINDOW);
                if (widget != null) {
                    widget.click(true);
                }
                break;
        }
        return -1;
    }

    public boolean myClickContinue() { // this method is probably going to throw a billion nullpointers but yolo
        Utilities.sleepNoException(500, 1000);
        return Widgets.getWidget(243, 7).click(true)
                || Widgets.getWidget(241, 5).click(true)
                || Widgets.getWidget(242, 6).click(true)
                || Widgets.getWidget(244, 8).click(true)
                || Widgets.getWidget(64, 5).click(true);
    }
}
