package ms.aurora.api.random.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.*;
import ms.aurora.api.methods.filters.NpcFilters;
import ms.aurora.api.methods.filters.ObjectFilters;
import ms.aurora.api.methods.tabs.Bank;
import ms.aurora.api.random.Random;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.api.wrappers.RSWidgetGroup;

/**
 * @author tobiewarburton
 */
public class CapnArnav extends Random {
    private static final int CAPTAIN_ID = 2308;
    private static final int PORTAL_ID = 11369;
    public int startValue3;
    public int startValue2;
    public int startValue1;

    public int currValue3;
    public int currValue2;
    public int currValue1;

    private boolean thirdColFound = false;
    private boolean secondColFound = false;
    private boolean firstColFound = false;

    private boolean reel1done = false;
    private boolean reel2done = false;
    private boolean reel3done = false;

    private boolean talkedto = false;

    private boolean done = false;


    private static class Interfaces {
        public static final int PARENT = 185;
        public static final int FIRST_SET_UP = 3;
        public static final int FIRST_SET_DOWN = 2;
        public static final int SECOND_SET_UP = 10;
        public static final int SECOND_SET_DOWN = 9;
        public static final int THIRD_SET_UP = 17;
        public static final int THIRD_SET_DOWN = 16;
        public static final int CONFIRM_BUTTON = 28;
    }

    @Override
    public boolean activate() {
        if(!Context.isLoggedIn()) return false;
        return Npcs.get(NpcFilters.ID(CAPTAIN_ID)) != null && Objects.get(ObjectFilters.ID(PORTAL_ID)) != null;
    }

    @Override
    public int loop() {
        RSNPC captain = Npcs.get(NpcFilters.ID(CAPTAIN_ID));
        Bank.close();
        if (!activate()) return -1;
        if (Players.getLocal().isMoving() || (Players.getLocal().getAnimation() != -1)) {
            Utilities.sleepNoException(500, 1000);
        }

        if (searchText(241, "yer foot")) {
            final RSObject chest = Objects.get(ObjectFilters.ID(2337));
            talkedto = true;
            chest.applyAction("Open");
            //try and wait a few (3-6) seconds for the interface to pop up
            for (int i = 0; i < 30 && !Widgets.getWidgets(Interfaces.PARENT).isValid(); i++) {
                Utilities.sleepNoException(100, 200);
            }
            return Utilities.random(800, 1200);
        }
        if (Widgets.getWidgets(228).getWidgets()[3] != null
                && Widgets.getWidgets(228).getWidgets()[3].getText().contains("Okay")) {
            Widgets.getWidgets(228).getWidgets()[3].click(true);
            for (int i = 0; i < 30 && Widgets.getWidgets(228).getWidgets()[3] != null
                    && Widgets.getWidgets(228).getWidgets()[3].getText().contains("Okay");
                 i++)
                Utilities.sleepNoException(100, 200);
        }
        if (Widgets.getWidgets(241).isValid() && Widgets.getWidgets(241).getWidgets()[0].getY() > 2) {
            if (searchText(231, "haul") || searchText(241, "Just hop")) {
                done = true;
                if (Widgets.canContinue()) {
                    Widgets.clickContinue();
                    return Utilities.random(600, 700);
                }
                return Utilities.random(500, 700);
            }
        }
        if (done) {
            final RSObject portal = Objects.get(ObjectFilters.ID(11369));
            if (portal == null) {
                return Utilities.random(800, 1200);
            }
            portal.applyAction("Enter");
        }
        if (Widgets.getWidgets(Interfaces.PARENT).isValid()) {

            for (int i = 0; i < 100 && !firstColFound; i++) {
                for (int j = 0; j < 100 && !thirdColFound; j++) {
                    startValue3 = Settings.getSetting(809);
                    Utilities.sleepNoException(Utilities.random(500, 700));
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.THIRD_SET_UP).click(true); // third set up
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    currValue3 = Settings.getSetting(809);
                    if (currValue3 < startValue3) {
                        thirdColFound = true;
                    }
                }

                for (int j = 0; j < 100 && !secondColFound; j++) {
                    startValue2 = Settings.getSetting(809);
                    Utilities.sleepNoException(Utilities.random(500, 700));
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.SECOND_SET_UP).click(true); // second set up
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    currValue2 = Settings.getSetting(809);
                    if (currValue2 < startValue2) {
                        secondColFound = true;
                    }
                }

                for (int j = 0; j < 100 && !firstColFound; j++) {
                    startValue1 = Settings.getSetting(809);
                    Utilities.sleepNoException(Utilities.random(500, 700));
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.FIRST_SET_UP).click(true); // first set up
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    currValue1 = Settings.getSetting(809);
                    if (currValue1 < startValue1) {
                        firstColFound = true;
                    }
                }
            }
        }

        if (Widgets.getWidgets(Interfaces.PARENT).isValid()) {
            if (searchText(Interfaces.PARENT, "Bar")) {
                for (int i = 0; i < 100 && !reel1done; i++) {
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.FIRST_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.FIRST_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel1done = true;
                }

                for (int i = 0; i < 100 && !reel2done; i++) {
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.SECOND_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.SECOND_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel2done = true;
                }

                for (int i = 0; i < 100 && !reel3done; i++) {
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.THIRD_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.THIRD_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel3done = true;
                }

                if (Widgets.getWidgets(Interfaces.PARENT).isValid()) {
                    Widgets.getWidgets(Interfaces.PARENT).getWidgets()[Interfaces.CONFIRM_BUTTON].click(true);
                    Utilities.sleepNoException(Utilities.random(700, 1000));
                }
            }
        }

        if (Widgets.getWidgets(Interfaces.PARENT).isValid()) {
            if (searchText(Interfaces.PARENT, "Coins")) {
                if (!reel1done) {

                    reel1done = true;
                }

                if (!reel2done) {

                    reel2done = true;
                }

                if (!reel3done) {

                    reel3done = true;
                }

                if (Widgets.getWidgets(Interfaces.PARENT).isValid()) {
                    Widgets.getWidgets(Interfaces.PARENT).getWidgets()[Interfaces.CONFIRM_BUTTON].click(true);
                    Utilities.sleepNoException(Utilities.random(700, 1000));
                }
            }
        }

        if (Widgets.getWidgets(Interfaces.PARENT).isValid()) {
            if (searchText(Interfaces.PARENT, "Bowl")) {
                for (int i = 0; i < 100 && !reel1done; i++) {
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.FIRST_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel1done = true;
                }

                for (int i = 0; i < 100 && !reel2done; i++) {
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.SECOND_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel2done = true;
                }

                for (int i = 0; i < 100 && !reel3done; i++) {
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.THIRD_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel3done = true;
                }

                if (Widgets.getWidgets(Interfaces.PARENT).isValid()) {
                    Widgets.getWidgets(Interfaces.PARENT).getWidgets()[Interfaces.CONFIRM_BUTTON].click(true); //click confirm
                    Utilities.sleepNoException(Utilities.random(700, 1000));
                }
            }
        }

        if (Widgets.getWidgets(Interfaces.PARENT).isValid()) { //scroll down
            if (searchText(Interfaces.PARENT, "Ring")) {
                for (int i = 0; i < 100 && !reel1done; i++) { //first set
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.FIRST_SET_DOWN).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel1done = true;
                }

                for (int i = 0; i < 100 && !reel2done; i++) {
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.SECOND_SET_DOWN).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel2done = true;
                }

                for (int i = 0; i < 100 && !reel3done; i++) {
                    Widgets.getWidget(Interfaces.PARENT, Interfaces.THIRD_SET_DOWN).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel3done = true;
                }

                if (Widgets.getWidgets(Interfaces.PARENT).isValid()) {
                    Widgets.getWidgets(Interfaces.PARENT).getWidgets()[Interfaces.CONFIRM_BUTTON].click(true);
                    Utilities.sleepNoException(Utilities.random(700, 1000));
                }
            }
        }
        if (Widgets.getWidgets(228).isValid() && Widgets.getWidgets(228).getWidgets()[0].getY() > 2) {
            // final int x = random(220, 310), y = random(427, 437);
            // mouse.click(x, y, true);
            Widgets.clickContinue();
        }
        if (!myClickContinue() && !talkedto && !Widgets.canContinue()) {
            captain.applyAction("Talk-to");
            return Utilities.random(500, 700);
        }
        if (Widgets.canContinue()) {
            Widgets.clickContinue();
            return Utilities.random(1000, 1200);
        }
        if (!done && talkedto && !Widgets.getWidgets(Interfaces.PARENT).isValid()
                && !Widgets.getWidgets(241).isValid()
                && !Widgets.canContinue()
                && Players.getLocal().getInteracting() == null) {
            captain.applyAction("Talk-to");
            return Utilities.random(500, 700);
        }
        return 500;
    }

    public boolean myClickContinue() {
        Utilities.sleepNoException(600, 1000);
        if ((Widgets.getWidget(243, 7) != null && Widgets.getWidget(243, 7).getY() < 5)
                || (Widgets.getWidget(241, 5) != null && Widgets.getWidget(241, 5).getY() < 5)
                || (Widgets.getWidget(242, 6) != null && Widgets.getWidget(242, 6).getY() < 5)
                || (Widgets.getWidget(244, 8) != null && Widgets.getWidget(244, 8).getY() < 5)
                || (Widgets.getWidget(64, 5) != null && Widgets.getWidget(64, 5).getY() < 5)
                || (Widgets.getWidget(236, 1) != null && Widgets.getWidget(236, 1).getY() < 5)
                || (Widgets.getWidget(230, 4) != null && Widgets.getWidget(230, 4).getY() < 5)
                || (Widgets.getWidget(228, 3) != null && Widgets.getWidget(228, 3).getY() < 5))
            return false;
        return Widgets.getWidget(243, 7).click(true)
                || Widgets.getWidget(241, 5).click(true)
                || Widgets.getWidget(242, 6).click(true)
                || Widgets.getWidget(244, 8).click(true)
                || Widgets.getWidget(64, 5).click(true)
                || Widgets.getWidget(236, 1).click(true)
                || Widgets.getWidget(230, 4).click(true)
                || Widgets.getWidget(228, 3).click(true);
    }

    public boolean searchText(int interfac, String text) {
        RSWidgetGroup talkFace = Widgets.getWidgets(interfac);
        if (talkFace == null)
            return false;
        for (int i = 0; i < talkFace.getWidgets().length; i++) {
            if (talkFace.getWidgets()[i].getText().contains(text)) {
                return true;
            }
        }
        return false;
    }
}
