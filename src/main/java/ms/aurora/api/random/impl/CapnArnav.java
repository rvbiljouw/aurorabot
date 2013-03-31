package ms.aurora.api.random.impl;

import ms.aurora.api.methods.filters.NpcFilters;
import ms.aurora.api.methods.filters.ObjectFilters;
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
        return npcs.get(NpcFilters.ID(CAPTAIN_ID)) != null && objects.get(ObjectFilters.ID(PORTAL_ID)) != null;
    }

    @Override
    public int loop() {
        RSNPC captain = npcs.get(NpcFilters.ID(CAPTAIN_ID));
        bank.close();
        if (!activate()) return -1;
        if (players.getLocal().isMoving() || (players.getLocal().getAnimation() != -1)) {
            Utilities.sleepNoException(500, 1000);
        }

        if (searchText(241, "yer foot")) {
            final RSObject chest = objects.get(ObjectFilters.ID(2337));
            talkedto = true;
            chest.applyAction("Open");
            //try and wait a few (3-6) seconds for the interface to pop up
            for (int i = 0; i < 30 && !widgets.getWidgets(Interfaces.PARENT).isValid(); i++) {
                Utilities.sleepNoException(100, 200);
            }
            return Utilities.random(800, 1200);
        }
        if (widgets.getWidgets(228).getWidgets()[3] != null
                && widgets.getWidgets(228).getWidgets()[3].getText().contains("Okay")) {
            widgets.getWidgets(228).getWidgets()[3].click(true);
            for (int i = 0; i < 30 && widgets.getWidgets(228).getWidgets()[3] != null
                    && widgets.getWidgets(228).getWidgets()[3].getText().contains("Okay");
                 i++)
                Utilities.sleepNoException(100, 200);
        }
        if (widgets.getWidgets(241).isValid() && widgets.getWidgets(241).getWidgets()[0].getY() > 2) {
            if (searchText(231, "haul") || searchText(241, "Just hop")) {
                done = true;
                if (widgets.canContinue()) {
                    widgets.clickContinue();
                    return Utilities.random(600, 700);
                }
                return Utilities.random(500, 700);
            }
        }
        if (done) {
            final RSObject portal = objects.get(ObjectFilters.ID(11369));
            if (portal == null) {
                return Utilities.random(800, 1200);
            }
            portal.applyAction("Enter");
        }
        if (widgets.getWidgets(Interfaces.PARENT).isValid()) {

            for (int i = 0; i < 100 && !firstColFound; i++) {
                for (int j = 0; j < 100 && !thirdColFound; j++) {
                    startValue3 = settings.getSetting(809);
                    Utilities.sleepNoException(Utilities.random(500, 700));
                    widgets.getWidget(Interfaces.PARENT, Interfaces.THIRD_SET_UP).click(true); // third set up
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    currValue3 = settings.getSetting(809);
                    if (currValue3 < startValue3) {
                        thirdColFound = true;
                    }
                }

                for (int j = 0; j < 100 && !secondColFound; j++) {
                    startValue2 = settings.getSetting(809);
                    Utilities.sleepNoException(Utilities.random(500, 700));
                    widgets.getWidget(Interfaces.PARENT, Interfaces.SECOND_SET_UP).click(true); // second set up
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    currValue2 = settings.getSetting(809);
                    if (currValue2 < startValue2) {
                        secondColFound = true;
                    }
                }

                for (int j = 0; j < 100 && !firstColFound; j++) {
                    startValue1 = settings.getSetting(809);
                    Utilities.sleepNoException(Utilities.random(500, 700));
                    widgets.getWidget(Interfaces.PARENT, Interfaces.FIRST_SET_UP).click(true); // first set up
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    currValue1 = settings.getSetting(809);
                    if (currValue1 < startValue1) {
                        firstColFound = true;
                    }
                }
            }
        }

        if (widgets.getWidgets(Interfaces.PARENT).isValid()) {
            if (searchText(Interfaces.PARENT, "Bar")) {
                for (int i = 0; i < 100 && !reel1done; i++) {
                    widgets.getWidget(Interfaces.PARENT, Interfaces.FIRST_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    widgets.getWidget(Interfaces.PARENT, Interfaces.FIRST_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel1done = true;
                }

                for (int i = 0; i < 100 && !reel2done; i++) {
                    widgets.getWidget(Interfaces.PARENT, Interfaces.SECOND_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    widgets.getWidget(Interfaces.PARENT, Interfaces.SECOND_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel2done = true;
                }

                for (int i = 0; i < 100 && !reel3done; i++) {
                    widgets.getWidget(Interfaces.PARENT, Interfaces.THIRD_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));
                    widgets.getWidget(Interfaces.PARENT, Interfaces.THIRD_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel3done = true;
                }

                if (widgets.getWidgets(Interfaces.PARENT).isValid()) {
                    widgets.getWidgets(Interfaces.PARENT).getWidgets()[Interfaces.CONFIRM_BUTTON].click(true);
                    Utilities.sleepNoException(Utilities.random(700, 1000));
                }
            }
        }

        if (widgets.getWidgets(Interfaces.PARENT).isValid()) {
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

                if (widgets.getWidgets(Interfaces.PARENT).isValid()) {
                    widgets.getWidgets(Interfaces.PARENT).getWidgets()[Interfaces.CONFIRM_BUTTON].click(true);
                    Utilities.sleepNoException(Utilities.random(700, 1000));
                }
            }
        }

        if (widgets.getWidgets(Interfaces.PARENT).isValid()) {
            if (searchText(Interfaces.PARENT, "Bowl")) {
                for (int i = 0; i < 100 && !reel1done; i++) {
                    widgets.getWidget(Interfaces.PARENT, Interfaces.FIRST_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel1done = true;
                }

                for (int i = 0; i < 100 && !reel2done; i++) {
                    widgets.getWidget(Interfaces.PARENT, Interfaces.SECOND_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel2done = true;
                }

                for (int i = 0; i < 100 && !reel3done; i++) {
                    widgets.getWidget(Interfaces.PARENT, Interfaces.THIRD_SET_UP).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel3done = true;
                }

                if (widgets.getWidgets(Interfaces.PARENT).isValid()) {
                    widgets.getWidgets(Interfaces.PARENT).getWidgets()[Interfaces.CONFIRM_BUTTON].click(true); //click confirm
                    Utilities.sleepNoException(Utilities.random(700, 1000));
                }
            }
        }

        if (widgets.getWidgets(Interfaces.PARENT).isValid()) { //scroll down
            if (searchText(Interfaces.PARENT, "Ring")) {
                for (int i = 0; i < 100 && !reel1done; i++) { //first set
                    widgets.getWidget(Interfaces.PARENT, Interfaces.FIRST_SET_DOWN).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel1done = true;
                }

                for (int i = 0; i < 100 && !reel2done; i++) {
                    widgets.getWidget(Interfaces.PARENT, Interfaces.SECOND_SET_DOWN).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel2done = true;
                }

                for (int i = 0; i < 100 && !reel3done; i++) {
                    widgets.getWidget(Interfaces.PARENT, Interfaces.THIRD_SET_DOWN).click(true);
                    Utilities.sleepNoException(Utilities.random(800, 1000));

                    reel3done = true;
                }

                if (widgets.getWidgets(Interfaces.PARENT).isValid()) {
                    widgets.getWidgets(Interfaces.PARENT).getWidgets()[Interfaces.CONFIRM_BUTTON].click(true);
                    Utilities.sleepNoException(Utilities.random(700, 1000));
                }
            }
        }
        if (widgets.getWidgets(228).isValid() && widgets.getWidgets(228).getWidgets()[0].getY() > 2) {
            // final int x = random(220, 310), y = random(427, 437);
            // mouse.click(x, y, true);
            widgets.clickContinue();
        }
        if (!myClickContinue() && !talkedto && !widgets.canContinue()) {
            captain.applyAction("Talk-to");
            return Utilities.random(500, 700);
        }
        if (widgets.canContinue()) {
            widgets.clickContinue();
            return Utilities.random(1000, 1200);
        }
        if (!done && talkedto && !widgets.getWidgets(Interfaces.PARENT).isValid()
                && !widgets.getWidgets(241).isValid()
                && !widgets.canContinue()
                && players.getLocal().getInteracting() == null) {
            captain.applyAction("Talk-to");
            return Utilities.random(500, 700);
        }
        return 500;
    }

    public boolean myClickContinue() {
        Utilities.sleepNoException(600, 1000);
        if ((widgets.getWidget(243, 7) != null && widgets.getWidget(243, 7).getY() < 5)
                || (widgets.getWidget(241, 5) != null && widgets.getWidget(241, 5).getY() < 5)
                || (widgets.getWidget(242, 6) != null && widgets.getWidget(242, 6).getY() < 5)
                || (widgets.getWidget(244, 8) != null && widgets.getWidget(244, 8).getY() < 5)
                || (widgets.getWidget( 64, 5) != null && widgets.getWidget(64 , 5).getY() < 5)
                || (widgets.getWidget(236, 1) != null && widgets.getWidget(236, 1).getY() < 5)
                || (widgets.getWidget(230, 4) != null && widgets.getWidget(230, 4).getY() < 5)
                || (widgets.getWidget(228, 3) != null && widgets.getWidget(228, 3).getY() < 5))
            return false;
        return widgets.getWidget(243, 7).click(true)
                || widgets.getWidget(241, 5).click(true)
                || widgets.getWidget(242, 6).click(true)
                || widgets.getWidget(244, 8).click(true)
                || widgets.getWidget( 64, 5).click(true)
                || widgets.getWidget(236, 1).click(true)
                || widgets.getWidget(230, 4).click(true)
                || widgets.getWidget(228, 3).click(true);
    }

    public boolean searchText(int interfac, String text) {
        RSWidgetGroup talkFace = widgets.getWidgets(interfac);
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
