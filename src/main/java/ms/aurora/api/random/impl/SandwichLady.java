package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Camera;
import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.random.Random;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.NPC;
import ms.aurora.api.wrappers.Widget;

/**
 * @author iJava
 */
public class SandwichLady extends Random {

    private static final int NPC_ID = 3117;
    private static final int WIDGET_PARENT_ID = 297;
    private static final int WIDGET_CHILD_ID = 8;
    private int modelId = -1;

    @Override
    public boolean activate() {
        return Npcs.get(new Predicate<NPC>() {
            @Override
            public boolean apply(NPC object) {
                return object.getId() == NPC_ID && object.getMessage().contains(Players.getLocal().getName());
            }
        }) != null;
    }

    @Override
    public int loop() {
        if (Widgets.getWidget(WIDGET_PARENT_ID, WIDGET_CHILD_ID) == null && Players.getLocal().getInteracting() == null) {
            NPC lady = Npcs.get(new Predicate<NPC>() {
                @Override
                public boolean apply(NPC object) {
                    return object.getId() == NPC_ID;
                }
            });
            if (lady != null) {
                if (!lady.isOnScreen()) {
                    Camera.turnTo(lady);
                    return 500;
                }
                lady.applyAction("Talk-to");
                return 1200;
            }
        }
        if (Widgets.getWidget(WIDGET_PARENT_ID, WIDGET_CHILD_ID) != null && Players.getLocal().getInteracting() != null) {
            String foodMessage = Widgets.getWidget(WIDGET_PARENT_ID, WIDGET_PARENT_ID).getText();
            Sandwiches sandwich = Sandwiches.getModelIdFor(foodMessage);
            if (sandwich != null) {
                modelId = sandwich.getModelId();
                Widget sandwichWidget = getWidget(modelId);
                if (sandwichWidget != null) {
                    sandwichWidget.click(true);
                    return 1500;
                }
            }
        }
        if (Widgets.canContinue() && modelId != -1) {
            Widgets.clickContinue();
            return 1500;
        }
        return 1000;
    }


    private Widget getWidget(int modelId) {
        for (int i = 1; i < 8; i++) {
            Widget sandwich = Widgets.getWidget(WIDGET_PARENT_ID, i);
            if (sandwich != null) {
                if (sandwich.getModelId() == modelId) {
                    return sandwich;
                }
            }
        }
        return null;
    }

    static enum Sandwiches {

        SQUARE(10731, "square"),
        ROLL(10727, "roll"),
        CHOCOLATE(10728, "chocolate"),
        BAGUETTE(10726, "baguette"),
        TRIANGLE(10732, "triangle"),
        KEBAB(10729, "kebab"),
        PIE(10730, "pie");

        private final int modelId;
        private final String name;

        Sandwiches(int modelId, String name) {
            this.modelId = modelId;
            this.name = name;
        }

        public int getModelId() {
            return modelId;
        }

        public String getMessage() {
            return name;
        }

        public static Sandwiches getModelIdFor(String name) {
            for (Sandwiches sandwich : Sandwiches.values()) {
                if (name.equals(sandwich.getMessage())) {
                    return sandwich;
                }
            }
            return null;
        }
    }


}
