package ms.aurora.api.random.impl;

import ms.aurora.api.methods.*;
import ms.aurora.api.methods.filters.NpcFilters;
import ms.aurora.api.methods.filters.ObjectFilters;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManfiest;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;

import static ms.aurora.api.util.Utilities.random;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author rvbiljouw
 */
@AfterLogin
@RandomManfiest(name = "Mr Morduat", version = 1.0)
public class MrMordaut extends Random {
    private static final int MR_MORDAUT_ID = 6117;

    private static final int DOOR_RED_ID = 2188;
    private static final int DOOR_BLUE_ID = 2189;
    private static final int DOOR_GREEN_ID = 2193;
    private static final int DOOR_PURPLE_ID = 2192;

    private RSWidgetGroup nextObjectInterface;
    private RSWidgetGroup relatedCardsInterface;
    private RSObject door;
    private final NextObjectQuestion nextObjectQuestion = new NextObjectQuestion();

    private final int[] Ranged = {11539, 11540, 11541, 11614, 11615, 11633};

    private final int[] Cooking = {11526, 11529, 11545, 11549, 11550, 11555, 11560,
            11563, 11564, 11607, 11608, 11616, 11620, 11621, 11622, 11623,
            11628,

            11629, 11634, 11639, 11641, 11649, 11624};

    private final int[] Fishing = {11527, 11574, 11578, 11580, 11599, 11600, 11601,
            11602, 11603,

            11604, 11605, 11606, 11625};

    private final int[] Combat = {11528, 11531, 11536, 11537, 11579, 11591, 11592,
            11593, 11597, 11627, 11631, 11635, 11636, 11638, 11642, 11648,
            11617};

    private final int[] Farming = {11530, 11532, 11547, 11548, 11554, 11556, 11571,
            11581, 11586, 11610, 11645, 11634, 11549, 11607, 11550};

    private final int[] Magic = {11533, 11534, 11538, 11562, 11567, 11582};

    private final int[] Firemaking = {11535, 11551, 11552, 11559, 11646};

    private final int[] Hats = {11540, 11557, 11558, 11560, 11570, 11619, 11626,
            11630, 11632, 11637, 11654};

    private final int[] Drinks = {11542, 11543, 11544, 11644, 11647};

    private final int[] Woodcutting = {11573, 11595};

    private final int[] Boots = {11561, 11618, 11650, 11651};

    private final int[] Crafting = {11546, 11553, 11565, 11566, 11568, 11569, 11572,
            11575, 11576, 11577, 11581, 11583, 11584, 11585, 11598, 11643, 11652,
            11653};

    private final int[] Mining = {11587, 11588, 11594, 11596, 11598, 11609, 11610, 11648, 11612};

    private final int[] Smithing = {11611, 11612, 11613};

    private final int[][] items = {Ranged, Cooking, Fishing, Combat, Farming, Magic,
            Firemaking, Hats, Drinks, Woodcutting, Boots, Crafting, Mining,
            Smithing};

    public final SimilarObjectQuestion[] simObjects = {
            new SimilarObjectQuestion("I'm feeling dehydrated", Drinks),
            new SimilarObjectQuestion("All this work is making me thirsty.", Drinks),
            new SimilarObjectQuestion("quenched my thirst", Drinks),
            new SimilarObjectQuestion("light my fire", Firemaking),
            new SimilarObjectQuestion("fishy", Fishing),
            new SimilarObjectQuestion("fishing for answers", Fishing),
            new SimilarObjectQuestion("fish out of water", Drinks),
            new SimilarObjectQuestion("I'm feeling dehydrated", Drinks),
            new SimilarObjectQuestion("strange headgear", Hats),
            new SimilarObjectQuestion("tip my hat", Hats),
            new SimilarObjectQuestion("thinking cap", Hats),
            new SimilarObjectQuestion("wizardry here", Magic),
            new SimilarObjectQuestion("rather mystical", Magic),
            new SimilarObjectQuestion("abracada", Magic),
            new SimilarObjectQuestion("hide one's face", Hats),
            new SimilarObjectQuestion("shall unmask", Hats),
            new SimilarObjectQuestion("hand-to-hand", Combat),
            new SimilarObjectQuestion("melee weapon", Combat),
            new SimilarObjectQuestion("me hearties", Hats),
            new SimilarObjectQuestion("mighty pirate", Drinks),
            new SimilarObjectQuestion("mighty archer", Ranged),
            new SimilarObjectQuestion("as an arrow", Ranged),
            new SimilarObjectQuestion("shiny things", Crafting),

            // Default
            new SimilarObjectQuestion("range", Ranged),
            new SimilarObjectQuestion("arrow", Ranged),
            new SimilarObjectQuestion("drink", Drinks),
            new SimilarObjectQuestion("logs", Firemaking),
            new SimilarObjectQuestion("light", Firemaking),
            new SimilarObjectQuestion("headgear", Hats),
            new SimilarObjectQuestion("hat", Hats),
            new SimilarObjectQuestion("cap", Hats),
            new SimilarObjectQuestion("mine", Mining),
            new SimilarObjectQuestion("mining", Mining),
            new SimilarObjectQuestion("ore", Mining),
            new SimilarObjectQuestion("fish", Fishing),
            new SimilarObjectQuestion("fishing", Fishing),
            new SimilarObjectQuestion("thinking cap", Hats),
            new SimilarObjectQuestion("cooking", Cooking),
            new SimilarObjectQuestion("cook", Cooking),
            new SimilarObjectQuestion("bake", Cooking),
            new SimilarObjectQuestion("farm", Farming),
            new SimilarObjectQuestion("farming", Farming),
            new SimilarObjectQuestion("cast", Magic),
            new SimilarObjectQuestion("magic", Magic),
            new SimilarObjectQuestion("craft", Crafting),
            new SimilarObjectQuestion("boot", Boots),
            new SimilarObjectQuestion("chop", Woodcutting),
            new SimilarObjectQuestion("cut", Woodcutting),
            new SimilarObjectQuestion("tree", Woodcutting),

    };

    private class NextObjectQuestion {
        private int one = -1, two = -1, three = -1;

        public NextObjectQuestion() {
        }

        public boolean arrayContains(final int[] arr, final int i) {
            boolean returnt = false;
            for (final int num : arr) {
                if (num == i) {
                    returnt = true;
                }
            }

            return returnt;
        }

        public boolean clickAnswer() {
            int[] Answers;
            if ((Answers = returnAnswer()) == null) {
                return false;
            }

            for (int i = 12; i <= 15; i++) {
                if (arrayContains(Answers, nextObjectInterface.getWidgets()[i]
                        .getModelId())) {
                    nextObjectInterface.getWidgets()[i].click(true);
                    return true;
                }
            }

            return false;
        }

        public boolean getObjects() {
            one = -1;
            two = -1;
            three = -1;
            one = nextObjectInterface.getWidgets()[8].getModelId();
            two = nextObjectInterface.getWidgets()[9].getModelId();
            three = nextObjectInterface.getWidgets()[10].getModelId();

            return one != -1 && two != -1 && three != -1;
        }

        public void guess() {
            final int[] objects = new int[4];
            objects[0] = nextObjectInterface.getWidgets()[12].getModelId();
            objects[1] = nextObjectInterface.getWidgets()[13].getModelId();
            objects[2] = nextObjectInterface.getWidgets()[14].getModelId();
            objects[3] = nextObjectInterface.getWidgets()[15].getModelId();

            int lowest = 120;
            int click = 12;
            final int compare = returnAnswer()[0];
            if (compare <= 12) {
                nextObjectInterface.getWidgets()[(random(12, 15))].click(true);
                return;
            }

            for (int i = 0; i < objects.length; i++) {
                if (Math.abs(objects[i] - compare) <= lowest) {
                    lowest = Math.abs(objects[i] - compare);
                }
                click = 12 + i;
            }
            nextObjectInterface.getWidgets()[click].click(true);
        }

        public int[] returnAnswer() {
            final int[] count = new int[items.length];
            for (int i = 0; i < count.length; i++) {
                count[i] = 0;
            }

            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items[i].length; j++) {
                    if (items[i][j] == one) {
                        count[i]++;
                    }
                    if (items[i][j] == two) {
                        count[i]++;
                    }
                    if (items[i][j] == three) {
                        count[i]++;
                    }

                    if (count[i] >= 2) {
                        info("Answer Type Found!");
                        return items[i];
                    }
                }
            }

            return null;
        }
    }


    private class SimilarObjectQuestion {
        private final String question;
        private final int[] Answers;

        public SimilarObjectQuestion(final String q, final int[] Answers) {
            question = q.toLowerCase();
            this.Answers = Answers;
        }

        private boolean accept() {
            relatedCardsInterface.getWidgets()[26].click(true);
            return true;
        }

        private boolean activateCondition() {
            if (!relatedCardsInterface.isValid()) {
                info("It's not valid.. lolwut");
                return false;
            }

            if (relatedCardsInterface.getWidgets()[25].getText().toLowerCase().contains(question)) {
                info("Question keyword: " + question);
                return true;
            }

            return false;
        }

        private boolean clickObjects() {
            int count = 0;
            for (int i = 42; i <= 56; i++) {
                for (final int answer : Answers) {
                    RSWidget anInterface = relatedCardsInterface.getWidgets()[i];
                    if (anInterface != null && anInterface.getModelId() == answer) {
                        anInterface.click(true);
                        sleepNoException(400, 600);
                        count++;
                        if (count >= 3) {
                            return true;
                        }
                    }
                }
            }
            info("returns false");
            return false;
        }
    }

    @Override
    public boolean activate() {
        return Npcs.get(NpcFilters.ID(MR_MORDAUT_ID)) != null;
    }

    @Override
    public int loop() {
        RSNPC mordaut = Npcs.get(NpcFilters.ID(MR_MORDAUT_ID));
        if (mordaut == null)
            return -1;
        if (Players.getLocal().isMoving() || Players.getLocal().getAnimation() != -1) {
            return random(800, 1200);
        }
        if (door != null) {
            if (!door.applyAction("Open")) {
                Camera.setAngle(random(0, 359));
                return random(800, 1200);
            }
        }
        RSWidget[] inters = Widgets.getWidgetsWithText("door");
        if (inters != null && inters.length == 1) {
            RSWidget inter = inters[0];
            if (inter.getText().toLowerCase().contains("red")) {
                door = Objects.get(ObjectFilters.ID(DOOR_RED_ID));
            } else if (inter.getText().toLowerCase().contains("blue")) {
                door = Objects.get(ObjectFilters.ID(DOOR_BLUE_ID));
            } else if (inter.getText().toLowerCase().contains("purple")) {
                door = Objects.get(ObjectFilters.ID(DOOR_PURPLE_ID));
            } else if (inter.getText().toLowerCase().contains("green")) {
                door = Objects.get(ObjectFilters.ID(DOOR_GREEN_ID));
            }
        }

        nextObjectInterface = Widgets.getWidgets(103);
        if (nextObjectInterface != null && nextObjectInterface.isValid()) {
            info("Question type: Next object");
            if (nextObjectQuestion.getObjects()) {
                if (nextObjectQuestion.clickAnswer()) {
                    return random(800, 1200);
                } else {
                    nextObjectQuestion.guess();
                    return random(800, 1200);
                }
            }
        }

        relatedCardsInterface = Widgets.getWidgets(559);
        if (relatedCardsInterface != null && relatedCardsInterface.isValid()) {
            info("Question type: Similar objects");
            for (SimilarObjectQuestion obj : simObjects) {
                if (obj.activateCondition()) {
                    if (obj.clickObjects()) {
                        obj.accept();
                    }
                }
            }
            return random(800, 1200);
        }

        if (Widgets.canContinue()) {
            Widgets.clickContinue();
            return random(800, 3500);
        }
        return random(800, 3500);
    }
}
