package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.wrappers.Widget;
import ms.aurora.api.wrappers.WidgetGroup;

import static ms.aurora.api.util.Utilities.random;


/**
 * Created with IntelliJ IDEA.
 * User: alowaniak
 * Author: alowaniak
 * Date: 3-7-13
 * Time: 23:52
 * <p/>
 * This code is for educational purposes only.
 * I can not be held responsible for any event triggered by the use
 * - in the broadest sense of the word;
 * this includes but is not limited to seeing, distributing, executing and eating -
 * of this piece of code.
 */

/**
 * Solves the Quiz Master random.<br/>
 * <p/>
 * The one where you get asked to play "Odd One Out".
 * TODO: Do we want 1K or the mystery box? currently got 1K...
 */
@RandomManifest(name = "Odd One Out", author = "alowaniak", version = 0.1)
public class QuizMaster extends Random {

    private final static int QUIZ_WIDGET_GROUP_ID = 191;
    private final static int[] MODEL_WIDGET_IDXS = {5, 6, 7};

    private final int[] WEAPON_MODEL_IDS = {6191, 6192};
    private final int[] ARMOUR_MODEL_IDS = {6193, 6194};
    private final int[] FARMING_MODEL_IDS = {6195, 6196};
    private final int[] JEWELRY_MODEL_IDS = {6197, 6198};
    private final int[] FISH_MODEL_IDS = {6189, 6190};
    private final int[][] ALL_MODEL_IDS = {WEAPON_MODEL_IDS, ARMOUR_MODEL_IDS,
            FARMING_MODEL_IDS, JEWELRY_MODEL_IDS, FISH_MODEL_IDS};



    @Override
    public boolean activate() {
        return Npcs.find().named("Quiz Master").single() != null;
    }

    @Override
    public int loop() {
        Widget oddOne = getTheOddOne();
        if(oddOne != null) {
            oddOne.click(true);
        } else {
            Widget[] widgets = Widgets.getWidgetsWithText("1000 Coins");
            if(widgets.length > 0) {
                widgets[0].click(true);
            }
        }
        return random(600, 1000);
    }

    /**
     * Gets the Widget containing the odd item.<br/>
     * <p/>
     * Finds the odd one by looping over all item categories.
     * For each item in the category it goes through the widgets, if widget's model equals the item it's saved.
     * If it's found again then we continue to the next category.
     * Thus if there's a widget saved after going through a category, then that will be the odd one.
     *
     * @return The Widget containing the odd item or null if WidgetGroup not showing.
     */
    private Widget getTheOddOne() {
        WidgetGroup wg = Widgets.getWidgetGroup(QUIZ_WIDGET_GROUP_ID);
        if(wg == null || !wg.isValid())
            return null;

        categoriesLoop: for(int[] itemCategory : ALL_MODEL_IDS) {
            Widget oddOne = null;
            for(int item : itemCategory) {
                for(int widgetIdx : MODEL_WIDGET_IDXS) {
                    Widget w = wg.getWidgets()[widgetIdx];
                    if(item == w.getModelId()) {
                        if(oddOne != null)
                            continue categoriesLoop;
                        oddOne = w;
                    }
                }
            }
            if(oddOne != null)
                return oddOne;
        }
        return null;
    }
}
