package ms.aurora.api.random.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.GroundItems;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.random.Random;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSGroundItem;
import ms.aurora.api.wrappers.RSWidgetItem;

import java.util.Arrays;
import java.util.List;

/**
 * @author tobiewarburton
 */
//TODO equipment stuff
//TODO open inventory
public class AxeHandler extends Random {
    private static final List<Integer> AXE_HEADS = Arrays.asList(480, 482, 484, 486, 488, 490);
    private static final int HANDLE = 466;

    private static final Predicate<RSGroundItem> AXE_HEAD_PREDICATE = new Predicate<RSGroundItem>() {
        @Override
        public boolean apply(RSGroundItem object) {
            return AXE_HEADS.contains(object.getId());
        }
    };

    private static final Predicate<RSWidgetItem> AXE_HEAD_INV_PREDICATE = new Predicate<RSWidgetItem>() {
        @Override
        public boolean apply(RSWidgetItem object) {
            return AXE_HEADS.contains(object.getId());
        }
    };

    private boolean droppedItem = false;
    private int droppedItemId = -1;

    private final Predicate<RSGroundItem> DROPPED_ITEM_PREDICATE = new Predicate<RSGroundItem>() {
        @Override
        public boolean apply(RSGroundItem object) {
            return object.getId() == droppedItemId;
        }
    };

    @Override
    public boolean activate() {
        if(!Context.isLoggedIn()) return false;
        RSGroundItem axe = GroundItems.get(AXE_HEAD_PREDICATE);
        RSWidgetItem handle = Inventory.get(HANDLE);
        return handle != null && axe != null;
    }

    @Override
    public int loop() {
        if (Players.getLocal().isInCombat() || Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1) {
            return 200;
        }
        switch (getState()) {
            case DROP_ITEM:
                RSWidgetItem itemToDrop = Inventory.getAll()[0];
                droppedItemId = itemToDrop.getId();
                itemToDrop.applyAction("Drop");
                droppedItem = true;
                break;
            case PICKUP_DROPPED_ITEM:
                RSGroundItem itemToPickup = GroundItems.get(DROPPED_ITEM_PREDICATE);
                itemToPickup.applyAction("Pickup");
                break;
            case FIX:
                RSWidgetItem axeHead = Inventory.get(AXE_HEAD_INV_PREDICATE);
                RSWidgetItem axeHandle = Inventory.get(HANDLE);
                axeHandle.applyAction("Use");
                axeHead.applyAction("Use");
                break;
            case PICKUP_HEAD:
                RSGroundItem axeGroundItem = GroundItems.get(AXE_HEAD_PREDICATE);
                axeGroundItem.applyAction("Pickup");
                break;
            case EXIT:
                return -1;
        }
        return 200;
    }

    public State getState() {
        if (Inventory.isFull() && GroundItems.get(AXE_HEAD_PREDICATE) != null) {
            return State.DROP_ITEM;
        } else if (GroundItems.get(AXE_HEAD_PREDICATE) != null) {
            return State.PICKUP_HEAD;
        } else if (Inventory.contains(HANDLE) && Inventory.get(AXE_HEAD_INV_PREDICATE) != null) {
            return State.FIX;
        } else if (droppedItem && GroundItems.get(DROPPED_ITEM_PREDICATE) != null) {
            return State.PICKUP_DROPPED_ITEM;
        } else {
            return State.EXIT;
        }
    }

    private enum State {
        DROP_ITEM,
        PICKUP_DROPPED_ITEM,
        FIX,
        PICKUP_HEAD,
        EXIT
    }
}
