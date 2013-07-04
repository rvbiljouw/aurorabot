package ms.aurora.api.random.impl;

import ms.aurora.api.methods.*;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.wrappers.GameObject;
import ms.aurora.api.wrappers.NPC;
import ms.aurora.api.wrappers.Widget;

import java.util.HashMap;
import java.util.Map;

import static ms.aurora.api.methods.Players.getLocal;
import static ms.aurora.api.methods.Predicates.OBJECT_ID;
import static ms.aurora.api.util.Utilities.random;

/**
 * Created with IntelliJ IDEA.
 * User: alowaniak
 * Author: alowaniak
 * Date: 4-7-13
 * Time: 4:30
 * <p/>
 * This code is for educational purposes only.
 * I can not be held responsible for any event triggered by the use
 * - in the broadest sense of the word;
 * this includes but is not limited to seeing, distributing, executing and eating -
 * of this piece of code.
 */

/**
 * Solves the Drill Demon random.<br/>
 * <p/>
 * The one where you gotta do some exercises.
 * TODO: might want some "smoothing out" on clicking etc. like adding some sleeps and whatnot (see comments in loop)
 */
@RandomManifest(name = "Drill Demon", author = "alowaniak", version = 0.1)
public class DrillDemon extends Random {

    private final static Map<String, Character> taskNameToAbbreviation;
    static {
        taskNameToAbbreviation = new HashMap<String, Character>();
        taskNameToAbbreviation.put("sit ups", 'S');
        taskNameToAbbreviation.put("jog", 'J');
        taskNameToAbbreviation.put("star jumps", 'U');
        taskNameToAbbreviation.put("push ups", 'P');
    }

    /**
     * The setting id (or index rather) at which the value for the object order is stored.
     */
    private final static int SETTING_ID = 531;

    /**
     * Object order (from west to east) mapped to setting value.
     *
     * There's probably some logic or pattern in the value...
     * But I had plenty of time so I just kept messing up exercises to get all the orders.
     * Luckily 4! is only 24, thank god there aren't 5 different exercises ^.^
     */
    private final static Map<Integer, String> settingValToObjectOrder;
    static {
        settingValToObjectOrder = new HashMap<Integer, String>();
        settingValToObjectOrder.put(2201, "JPSU");
        settingValToObjectOrder.put(1697, "JUSP");
        settingValToObjectOrder.put(1809, "JSUP");
        settingValToObjectOrder.put(1305, "JPUS");
        settingValToObjectOrder.put(2257, "JSPU");
        settingValToObjectOrder.put(1249, "JUPS");
        settingValToObjectOrder.put(1634, "SUJP");
        settingValToObjectOrder.put(2138, "SPJU");
        settingValToObjectOrder.put(1802, "SJUP");
        settingValToObjectOrder.put(2250, "SJPU");
        settingValToObjectOrder.put(738, "SUPJ");
        settingValToObjectOrder.put(794, "SPUJ");
        settingValToObjectOrder.put(668, "UPSJ");
        settingValToObjectOrder.put(724, "USPJ");
        settingValToObjectOrder.put(1620, "USJP");
        settingValToObjectOrder.put(1228, "UJPS");
        settingValToObjectOrder.put(1116, "UPJS");
        settingValToObjectOrder.put(1676, "UJSP");
        settingValToObjectOrder.put(2187, "PJSU");
        settingValToObjectOrder.put(1123, "PUJS");
        settingValToObjectOrder.put(675, "PUSJ");
        settingValToObjectOrder.put(2131, "PSJU");
        settingValToObjectOrder.put(787, "PSUJ");
        settingValToObjectOrder.put(1291, "PJUS");
    }

    /**
     * The mat id's in west to east order.
     */
    private final static int[] MAT_IDS = {2647, 3637, 5551, 2648};

    /**
     * The task we currently have to perform
     */
    String currentTask;

    @Override
    public boolean activate() {
        if(Npcs.find().named("Sergeant Damien").single() != null)
            return true;
        currentTask = null;
        return false;
    }

    @Override
    public int loop() {
        if(getLocal().getAnimation() != -1) {
            //setting task null is needed because we don't find new task (since interface is not yet up) with this codes
            currentTask = null;
            //not sure what's nicer, this or
            // a while getAnimation() != -1 and a sleep after it so we don't click the sergeant after doing the exercise etc.
            return 50;
        }

        findCurrentTask();
        if(currentTask != null) {
            char abbreviation = taskNameToAbbreviation.get(currentTask);
            System.out.println("Found task:" + currentTask + "->" + abbreviation);
            int settingVal = Settings.get(SETTING_ID);
            String objectOrder = settingValToObjectOrder.get(settingVal);
            System.out.println("Setting: " + settingVal + "->" + objectOrder);
            int matIdx = objectOrder.indexOf(abbreviation);
            int matId = MAT_IDS[matIdx];
            System.out.println("Mat idx:" + matIdx + " id:" + matId);
            GameObject mat = Objects.get(OBJECT_ID(matId));
            if(mat != null) {
                if(mat.isOnScreen()) {
                    mat.click(true);
                } else {
                    Walking.walkTo(mat);
                }
            }
        } else if(Widgets.canContinue()) {
            Widgets.clickContinue();
        } else {
            //Guess we somehow walked away causing the widget shit to disappear without getting a task?
            //Or actually more probably we got here after doing the exercise before the interface was up yet
            NPC serg = Npcs.find().named("Sergeant Damien").single();
            if(serg != null) {
                if(serg.isOnScreen()) {
                    serg.applyAction("Talk");
                } else {
                    //Well we could walk here but that fucker keeps walking all the time too...
                }
            }
        }
        return random(600, 1200);
    }

    /**
     * Finds current task by looking for an interface which contains one of the task names.
     * Sets {@link #currentTask} if task found.
     *
     * @see #currentTask
     */
    private void findCurrentTask() {
        for(String taskName : taskNameToAbbreviation.keySet()) {
            Widget[] hits = Widgets.getWidgetsWithText(taskName);
            if(hits.length > 0) {
                currentTask = taskName;
                break;
            }
        }
    }
}