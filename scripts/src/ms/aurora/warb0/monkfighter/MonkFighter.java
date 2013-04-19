package ms.aurora.warb0.monkfighter;

import ms.aurora.api.methods.Skills;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.event.listeners.PaintListener;
import ms.aurora.warb0.monkfighter.action.*;
import ms.aurora.warb0.script.Action;
import ms.aurora.warb0.script.ActionScript;
import ms.aurora.warb0.script.util.Stopwatch;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author tobiewarburton
 */
@ScriptManifest(
        name = "Monk Fighter",
        version = 0.01,
        author = "vim",
        category = "Combat",
        shortDescription = "fights monks and heals rather than eats!"
)
public class MonkFighter extends ActionScript implements PaintListener {
    private Map<Skills.Skill, Integer> startXpMap = new HashMap<Skills.Skill, Integer>();
    private Skills.Skill[] skills = new Skills.Skill[]{
            Skills.Skill.ATTACK, Skills.Skill.DEFENCE, Skills.Skill.STRENGTH, Skills.Skill.RANGED,
            Skills.Skill.MAGIC, Skills.Skill.HITPOINTS
    };
    private Stopwatch stopwatch = Stopwatch.create();


    @Override
    public Action[] actions() {
        return new Action[]{ // order is important here
                new HealAction(), // better not die!!!!
                new PickupAction(), // gotta make money
                new FightAction() // gotta kill monsters
        };
    }

    @Override
    public void onStart() {
        // show ui
        for (Skills.Skill skill : skills) {
            startXpMap.put(skill, Skills.getExperience(skill));
        }
        stopwatch.start();
    }

    private Map<Skills.Skill, Integer> getGainedXp() {
        Map<Skills.Skill, Integer> map = new HashMap<Skills.Skill, Integer>();
        for (Skills.Skill skill : skills) {
            int gain = Skills.getExperience(skill) - startXpMap.get(skill);
            if (gain > 0) {
                map.put(skill, gain);
            }
        }
        return map;
    }

    private int sum(Collection<Integer> elements) {
        int sum = 0;
        for (Integer e : elements) {
            sum += e;
        }
        return sum;
    }

    @Override
    public void onRepaint(Graphics graphics) {
        Map<Skills.Skill, Integer> gained = getGainedXp();
        int totalGained = sum(gained.values());
        int x = 20, y = 20;

        java.util.List<String> itemList = new LinkedList<String>(); // order preserved
        itemList.add(stopwatch.getPrettyFormat());
        itemList.add(String.format("Total XP Gain: %d", totalGained));
        itemList.add(String.format("Total XP Hour: %d", stopwatch.getHourlyRate(totalGained)));
        itemList.add("");
        for (Map.Entry<Skills.Skill, Integer> entry : gained.entrySet()) {
            String name = entry.getKey().toString();
            String prettyName = name.substring(0, 1) + name.substring(1).toLowerCase();
            int xp = entry.getValue();
            itemList.add(String.format("%s XP Gain: %d (%d p/h)", prettyName, xp, stopwatch.getHourlyRate(xp)));
        }

        graphics.setColor(new Color(61, 61, 61, 100));
        graphics.fillRoundRect(x - 10, y - 10, 200, itemList.size() * 20 + 10, 10, 10);
        graphics.setColor(Color.WHITE);

        y += 10;
        for (String item : itemList) {
            graphics.drawString(item, x, y);
            y += 20;
        }
    }
}
