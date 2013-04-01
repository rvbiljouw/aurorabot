import ms.aurora.api.methods.Skills;
import ms.aurora.api.methods.filters.ObjectFilters;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static ms.aurora.api.util.Utilities.random;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author tobiewarburton
 */
@ScriptManifest(name = "Power Miner",
        author = "warb0",
        version = 0.1,
        shortDescription = "mines dem rocks")
public class IronPowerMiner extends Script implements PaintListener {
    private static final int IRON_ROCK_ID[] = {2093, 2093, 2092, 9717, 9719, 9717, 9718, 11956, 11955,
            11954, 11557, 37307, 37309};

    private int miningStartXp = -1;
    private long startTime = -1;

    @Override
    public void onStart() {
        miningStartXp = skills.getExperience(Skills.Skill.MINING);
        startTime = System.currentTimeMillis();
    }

    @Override
    public int tick() {
        if (players.getLocal().isMoving()
                || players.getLocal().getAnimation() != -1) {
            return random(250, 500);
        }
        if (inventory.isFull()) {
            for (Inventory.InventoryItem ore : inventory.getAll(440)) {
                ore.applyAction("Drop");
                sleepNoException(200, 400);
            }
        }
        RSObject rock = objects.get(ObjectFilters.ID(IRON_ROCK_ID));
        if (rock != null) {
            rock.applyAction("Mine");
        }
        return random(100, 250);
    }

    @Override
    public void onRepaint(Graphics graphics) {
        long runtime = System.currentTimeMillis() - startTime;
        int secs = ((int) ((runtime / 1000) % 60));
        int mins = ((int) (((runtime / 1000) / 60) % 60));
        int hours = ((int) ((((runtime / 1000) / 60) / 60) % 60));
        int gained = skills.getExperience(Skills.Skill.MINING) - miningStartXp;
        float xpSecond = 0;
        if ((mins > 0 || hours > 0 || secs > 0) && gained > 0) {
            xpSecond = ((float) gained / (float) (secs + (mins * 60) + (hours * 60 * 60)));
        }
        float xpMinute = xpSecond * 60;
        int xpHour = (int) xpMinute * 60;

        int x = 20, y = 20;
        List<String> itemList = new LinkedList<String>(); // order preserved
        itemList.add(String.format("Runtime: %02d:%02d:%02d", hours, mins, secs));
        itemList.add(String.format("XP Gain: %d", gained));
        itemList.add(String.format("XP Hour: %d", xpHour));
        graphics.setColor(Color.CYAN);
        graphics.drawRect(x - 10, y - 10, 200, itemList.size() * 20 + 10);
        graphics.setColor(Color.BLACK);
        for (String item : itemList) {
            graphics.drawString(item, x, y += 20);
        }
    }
}
