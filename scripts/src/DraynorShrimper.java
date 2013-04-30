import ms.aurora.api.methods.*;
import ms.aurora.api.methods.tabs.Bank;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.api.util.Timer;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;
import java.util.LinkedList;

import static ms.aurora.api.methods.filters.NpcFilters.ID;
import static ms.aurora.api.util.Utilities.random;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * Date: 26/03/13
 * Time: 16:23
 *
 * @author A_C/Cov
 */
@ScriptManifest(
        name = "Draynor Shrimper",
        author = "Steam",
        version = 1.0,
        shortDescription = "Catches and banks shrimps in draynor",
        category = "Fishing"
)
public class DraynorShrimper extends Script implements PaintListener {

    // Tiles
    private static final RSTile FISHING_TILE = new RSTile(3087, 3229);
    private static final RSTile BANK_TILE = new RSTile(3094, 3243);

    // Paths
    private static final RSTile[] FISH_PATH = new RSTile[] {
            new RSTile(3094,3243), new RSTile(3091,3247),
            new RSTile(3087,3244), new RSTile(3087,3239),
            new RSTile(3087,3234), new RSTile(3087,3229)
    };
    private static final RSTile[] BANK_PATH = new RSTile[] {
            new RSTile(3087,3229), new RSTile(3090,3233),
            new RSTile(3088,3238), new RSTile(3087,3243),
            new RSTile(3090,3247), new RSTile(3094,3243)
    };

    // Ids
    private static final int FISHING_SPOT_ID = 327;
    private static final int[] FISH_IDS =  { 317, 321 };

    // Paint stoof
    private int startXp = -1;
    private int startLvl = -1;
    private Timer scriptTimer = null;

    private enum ScriptState { BANK, WALK_TO_FISH, FISH, WALK_TO_BANK };

    private ScriptState getScriptState() {
        if (Calculations.distance(Players.getLocal().getLocation(), FISHING_TILE) < 5) {
            return Inventory.isFull() ? ScriptState.WALK_TO_BANK : ScriptState.FISH;
        } else if (Calculations.distance(Players.getLocal().getLocation(), BANK_TILE) < 5) {
            return !Inventory.containsAny(FISH_IDS) ? ScriptState.WALK_TO_FISH : ScriptState.BANK;
        }
        return null;
    }

    @Override
    public int tick() {

        ScriptState state = this.getScriptState();

        if (state != null) {
            switch (state) {

                case BANK:
                    if (!Bank.isOpen()) {
                        if (Bank.open()) {
                            return random(500, 1000);
                        }
                    } else {
                        Bank.depositAll(FISH_IDS);

                    }
                    break;

                case WALK_TO_FISH:
                    Walking.traverse(FISH_PATH, Walking.FORWARDS);
                    //Walking.walkTo(FISHING_TILE);
                    break;

                case FISH:
                    if (Players.getLocal().getAnimation() == -1 && !Players.getLocal().isMoving()) {
                        RSNPC spot = Npcs.get(ID(FISHING_SPOT_ID));
                        if (spot != null) {
                            if (spot.applyAction("Net")) {
                                return random(500, 1000);
                            }
                        }
                    }
                    break;

                case WALK_TO_BANK:
                    Walking.traverse(FISH_PATH, Walking.BACKWARDS);
                    //Walking.walkTo(BANK_TILE);
                    break;

            }
        }
        return 0;
    }

    @Override
    public void onRepaint(Graphics g) {
        if (this.scriptTimer == null) {
            this.scriptTimer = new Timer();
        }
        if (this.startXp < 0) {
            this.startXp = Skills.getExperience(Skills.Skill.FISHING);
        }
        if (this.startLvl < 0) {
            this.startLvl = Skills.getLevel(Skills.Skill.FISHING);
        }
        LinkedList<String> paintStrings = new LinkedList<String>();
        paintStrings.add("Draynor Netter by Steam");
        paintStrings.add("Time ran: " + Timer.formatTime(this.scriptTimer.elapsed()));
        paintStrings.add("Exp gained: " + (Skills.getExperience(Skills.Skill.FISHING) - this.startXp));
        paintStrings.add("Levels gained: " + (Skills.getLevel(Skills.Skill.FISHING) - this.startLvl));
        paintStrings.add("Exp to Level: " +
                Skills.getExperienceToLevel(Skills.Skill.FISHING,
                        Skills.getLevel(Skills.Skill.FISHING) + 1));
        final int fontHeight = g.getFontMetrics().getHeight(),
                height = (paintStrings.size() + 1) * fontHeight;
        int x = 10, y = 10;
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(x, y, 200, height);
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(x, y, 200, height);
        x += 5;
        y += fontHeight;
        for (String s: paintStrings) {
            g.drawString(s, x, y);
            y += fontHeight;
        }
    }
}
