import ms.aurora.api.methods.*;
import ms.aurora.api.methods.filters.GroundItemFilters;
import ms.aurora.api.methods.filters.NpcFilters;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.util.Timer;
import ms.aurora.api.wrappers.RSArea;
import ms.aurora.api.wrappers.RSGroundItem;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

import static ms.aurora.api.util.Utilities.sleepUntil;

/**
 * Date: 18/04/13
 * Time: 09:00
 *
 * @author A_C/Cov
 */
@ScriptManifest(
        name = "Chicken Killer",
        author = "Steam",
        version = 0.1,
        shortDescription = "Kills chickens, loots feathers and buries bones",
        category = "Combat"
)
public class ChickenKiller extends Script implements PaintListener {

    private static final int FEATHER_ID = 314, BONE_ID = 526;
    private static final RSArea CHICKEN_AREA = new RSArea( new RSTile( 3021, 3298), new RSTile( 3013, 3281));

    private static final HashMap<Skills.Skill, Integer> SKILL_EXP_MAP = new HashMap<Skills.Skill, Integer>();
    private static final HashMap<Skills.Skill, Integer> SKILL_LVL_MAP = new HashMap<Skills.Skill, Integer>();
    private static final Skills.Skill[] SKILL_ARRAY = new Skills.Skill[] {
            Skills.Skill.HITPOINTS, Skills.Skill.ATTACK, Skills.Skill.STRENGTH, Skills.Skill.DEFENCE
    };

    private Timer scriptTimer;
    private String state = "Idle";

    private enum ScriptState { KILL, LOOT, BURY }

    private ScriptState getScriptState() {
        if (Inventory.contains(BONE_ID)) {
            this.state = "Burying Bones";
            return ScriptState.BURY;
        } else if (GroundItems.get(GroundItemFilters.ID(FEATHER_ID)) != null) {
            this.state = "Looting";
            return ScriptState.LOOT;
        } if (Npcs.get(NpcFilters.NAMED("Chicken")) != null && !Players.getLocal().isInCombat()) {
            this.state = "Killing Chickens";
            return ScriptState.KILL;
        }
        this.state = "Idle";
        return null;
    }

    @Override
    public int tick() {
        ScriptState state = this.getScriptState();
        if (state != null) {
            switch (state) {
                case KILL:
                    RSNPC chicken = Npcs.get(NpcFilters.NAMED("Chicken"), new Predicate<RSNPC>() {
                        @Override
                        public boolean apply(RSNPC object) {
                            return object.getInteracting() == null && CHICKEN_AREA.contains(object.getLocation());
                        }
                    });
                    if (chicken != null) {
                        if (!chicken.isOnScreen()) {
                            Walking.clickOnMap(chicken.getLocation());
                        }
                        if (chicken.applyAction("Attack")) {
                            sleepUntil(new StatePredicate() {
                                @Override
                                public boolean apply() {
                                    return Players.getLocal().isInCombat();
                                }
                            }, 3000);
                        }
                    }
                    break;
                case LOOT:
                    RSGroundItem loot = GroundItems.get(GroundItemFilters.ID(FEATHER_ID));
                    if (loot != null) {
                        if (!loot.isOnScreen()) {
                            Walking.clickOnMap(loot.getLocation());
                        }
                        final int invSize = Inventory.getCount();
                        loot.applyAction("(Bones|Feather)");
                        sleepUntil(new StatePredicate() {
                            @Override
                            public boolean apply() {
                                return Inventory.getCount() > invSize;
                            }
                        }, 2500);
                    }
                    break;
                case BURY:
                    Inventory.InventoryItem bones = Inventory.get(BONE_ID);
                    if (bones != null) {
                        final int invSize = Inventory.getCount();
                        bones.applyAction("Bury");
                        sleepUntil(new StatePredicate() {
                            @Override
                            public boolean apply() {
                                return Inventory.getCount() < invSize;
                            }
                        }, 1000);
                    }
                    break;
            }
        }
        return 0;
    }

    @Override
    public void onRepaint(Graphics g) {
        if (SKILL_EXP_MAP.size() == 0 && SKILL_LVL_MAP.size() == 0) {
            for (Skills.Skill skill: SKILL_ARRAY) {
                SKILL_EXP_MAP.put(skill, Skills.getExperience(skill));
                SKILL_LVL_MAP.put(skill, Skills.getLevel(skill));
            }
            this.scriptTimer = new Timer();
        }
        LinkedList<String> paintStrings = new LinkedList<String>();
        paintStrings.add("Chicken Killer by Steam");
        paintStrings.add("Time ran: " + Timer.formatTime(this.scriptTimer.elapsed()));
        paintStrings.add("Script State: " + this.state);

        for (Skills.Skill skill: SKILL_ARRAY) {
            int expGained = Skills.getExperience(skill) - SKILL_EXP_MAP.get(skill);
            if (expGained > 0) {
                paintStrings.add(skill.toString() + " Exp gained: " + expGained);
                paintStrings.add(skill.toString() + " Levels gained: " + (Skills.getLevel(skill) - SKILL_LVL_MAP.get(skill)));
                paintStrings.add(skill.toString() + " Exp to Level: " +
                        Skills.getExperienceToLevel(skill,
                                Skills.getLevel(skill) + 1));
            }
        }
        final int fontHeight = g.getFontMetrics().getHeight(),
                height = paintStrings.size() * fontHeight + 10;
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
