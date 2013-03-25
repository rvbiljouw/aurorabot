package ms.aurora.api.random.impl;

import ms.aurora.api.random.Random;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSGroundItem;

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

    private static final Predicate<Inventory.InventoryItem> AXE_HEAD_INV_PREDICATE = new Predicate<Inventory.InventoryItem>() {
        @Override
        public boolean apply(Inventory.InventoryItem object) {
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
        RSGroundItem axe = items.get(AXE_HEAD_PREDICATE);
        Inventory.InventoryItem handle = inventory.get(HANDLE);
        return handle != null && axe != null;
    }

    @Override
    public int loop() {
        if (players.getLocal().isInCombat() || players.getLocal().isMoving() || players.getLocal().getAnimation() != -1) {
            return 200;
        }
        switch (getState()) {
            case DROP_ITEM:
                Inventory.InventoryItem itemToDrop = inventory.getAll()[0];
                droppedItemId = itemToDrop.getId();
                itemToDrop.applyAction("Drop");
                droppedItem = true;
                break;
            case PICKUP_DROPPED_ITEM:
                RSGroundItem itemToPickup = items.get(DROPPED_ITEM_PREDICATE);
                itemToPickup.applyAction("Pickup");
                break;
            case FIX:
                Inventory.InventoryItem axeHead = inventory.get(AXE_HEAD_INV_PREDICATE);
                Inventory.InventoryItem axeHandle = inventory.get(HANDLE);
                axeHandle.applyAction("Use");
                axeHead.applyAction("Use");
                break;
            case PICKUP_HEAD:
                RSGroundItem axeGroundItem = items.get(AXE_HEAD_PREDICATE);
                axeGroundItem.applyAction("Pickup");
                break;
            case EXIT:
                return -1;
        }
        return 200;
    }

    public State getState() {
        if (inventory.isFull() && items.get(AXE_HEAD_PREDICATE) != null) {
            return State.DROP_ITEM;
        } else if (items.get(AXE_HEAD_PREDICATE) != null) {
            return State.PICKUP_HEAD;
        } else if (inventory.contains(HANDLE) && inventory.get(AXE_HEAD_INV_PREDICATE) != null) {
            return State.FIX;
        } else if (droppedItem && items.get(DROPPED_ITEM_PREDICATE) != null) {
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
