package ms.aurora.api.random.impl;

import ms.aurora.api.methods.*;
import ms.aurora.api.methods.Predicates;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.GameObject;
import ms.aurora.api.wrappers.GroundItem;
import ms.aurora.api.wrappers.NPC;
import ms.aurora.api.wrappers.Widget;

/**
 * Date: 02/07/13
 * Time: 12:09
 *
 * @author A_C/Cov
 */
@RandomManifest(name = "Freaky Forester", version = 1.0, author = "Cov")
public class FreakyForester extends Random {

    private static final int FREAKY_FORESTER_ID = 2458;
    private static final int BIRD_MEAT_ID = 6179;
    private static final int EXIT_PORTAL_ID = 8972;
    private static final int WIDGET_GROUP_ID = 243;
    private static final int WIDGET_ID = 2;


    private Pheasant bird = null;
    private boolean completed = false;

    private int getRandomState() {
        if (completed) {
            return 0;
        }
        if (bird == null) {
            return 1;
        } else {
            if (Inventory.contains(Predicates.WIDGETITEM_ID(BIRD_MEAT_ID))) {
                return 2;
            }
            if (GroundItems.find().id(BIRD_MEAT_ID).single() != null) {
                return 3;
            } else {
                return 4;
            }
        }
    }

    @Override
    public boolean activate() {
        return Npcs.find().id(FREAKY_FORESTER_ID).single() != null;
    }

    @Override
    public int loop() {

        switch (getRandomState()) {
            case 0:
                GameObject portal = Objects.find().id(EXIT_PORTAL_ID).single();
                if (portal != null) {
                    if (!portal.isOnScreen()) {
                        Walking.walkTo(portal);
                        Utilities.sleepUntil(PLAYER_NOT_MOVING(), 3000);
                    }
                    if (portal.applyAction("Enter")) {
                        bird = null;
                        return 0;
                    }
                }
                break;
            case 1:
                Widget textWidget = Widgets.getWidget(WIDGET_GROUP_ID, WIDGET_ID);
                if (textWidget != null) {
                    bird = Pheasant.getByText(textWidget.getText());
                } else {
                    npcInteract(Npcs.find().id(FREAKY_FORESTER_ID).single(), "Talk-to");
                }
                break;
            case 2:
                completed = npcInteract(Npcs.find().id(FREAKY_FORESTER_ID).single(), "Talk-to")
                        && !Inventory.contains(Predicates.WIDGETITEM_ID(BIRD_MEAT_ID));
                break;
            case 3:
                GroundItem birdMeat = GroundItems.find().id(BIRD_MEAT_ID).single();
                if (!birdMeat.isOnScreen()) {
                    Walking.walkTo(birdMeat);
                    Utilities.sleepUntil(PLAYER_NOT_MOVING(), 3000);
                }
                if (birdMeat.applyAction("Take")) {
                    Utilities.sleepUntil(INVENTORY_CHANGE(Inventory.getCount()), 3000);
                }
                break;
            case 4:
                if (npcInteract(Npcs.find().id(bird.getId()).single(), "Attack")) {
                    Utilities.sleepUntil(PLAYER_INTERACTING(), 3000);
                }
                break;
        }
        return 0;
    }

    private boolean npcInteract(NPC npc, String action) {
        if (npc == null) {
            return false;
        }
        if (!npc.isOnScreen()) {
            Walking.walkTo(npc);
            Utilities.sleepUntil(PLAYER_NOT_MOVING(), 3000);
        }
        if (npc.applyAction(action)) {
            Utilities.sleepUntil(PLAYER_INTERACTING(), 3000);
            return true;
        }
        return false;
    }

    private StatePredicate PLAYER_NOT_MOVING() {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return !Players.getLocal().isMoving();
            }
        };
    }

    private StatePredicate PLAYER_INTERACTING() {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return Players.getLocal().getInteracting() != null;
            }
        };
    }

    private StatePredicate INVENTORY_CHANGE(final int count) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return Inventory.getCount() != count;
            }
        };
    }

    private enum Pheasant {
        ONE("one", 2459),
        TWO("two", 2460),
        THREE("three", 2461),
        FOUR("four", 2462);

        private String text;
        private int id;

        Pheasant(String text, int id) {
            this.text = text;
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public int getId()  {
            return id;
        }

        public static Pheasant getByText(String text) {
            for (Pheasant pheasant : values()) {
                if (text.indexOf(pheasant.getText()) != -1) {
                    return pheasant;
                }
            }
            return null;
        }
    }
}
