package ms.aurora.warb0.flaxpicker;

import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Walking;
import ms.aurora.api.methods.tabs.Bank;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.util.Stopwatch;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.String.format;
import static ms.aurora.api.methods.filters.ObjectFilters.ID;
import static ms.aurora.api.util.Utilities.*;

/**
 * @author tobiewarburton
 */
@ScriptManifest(
        name = "Flax Picker",
        version = 0.01,
        author = "vim",
        category = "Money Making",
        shortDescription = "picks flax to make $"
)
public class FlaxPicker extends Script implements PaintListener {
    private static StatePredicate IDLE = new StatePredicate() {
        @Override
        public boolean apply() {
            return !Players.getLocal().isMoving();
        }
    };
    private static RSTile[] FLAX_TO_BANK = new RSTile[]{
            new RSTile(2738, 3443), new RSTile(2732, 3445), new RSTile(2730, 3451),
            new RSTile(2729, 3457), new RSTile(2725, 3462), new RSTile(2723, 3468),
            new RSTile(2723, 3475), new RSTile(2727, 3481), new RSTile(2718, 3526),
            new RSTile(2726, 3486)
    };

    private static int FLAX_OBJ_ID = 3634;
    private static int BANK_OBJ_ID = 25808;
    private static int FLAX_ID = 1779;
    private static int PRICE = 30;

    private Stopwatch stopwatch = Stopwatch.create();
    private Random random = new Random();
    private int totalGained;

    @Override
    public void onStart() {
        stopwatch.start();
    }

    @Override
    public int tick() {
        if (Inventory.isFull()) {
            RSObject bank = Objects.get(ID(BANK_OBJ_ID));
            if (!Bank.isOpen()) {
                if (bank != null && bank.isOnScreen()) {
                    bank.applyAction("Bank");
                    sleepUntil(IDLE);
                } else {
                    Walking.traverse(FLAX_TO_BANK, Walking.FORWARDS);
                }
            } else {
                totalGained += Inventory.count(FLAX_ID);
                Bank.depositAll(FLAX_ID);
                sleepNoException(100, 200);
                if (random.nextBoolean()) {
                    Bank.close();
                }
            }
        } else {
            sleepUntil(IDLE, 5000);
            RSObject flax = Objects.get(ID(FLAX_OBJ_ID));
            if (flax != null && flax.isOnScreen()) {
                flax.applyAction("Pick");
            } else {
                Walking.traverse(FLAX_TO_BANK, Walking.BACKWARDS);
            }
        }
        return random(200, 300);
    }

    @Override
    public void onRepaint(Graphics graphics) {
        int x = 20, y = 20;

        java.util.List<String> itemList = new LinkedList<String>(); // order preserved
        itemList.add(stopwatch.getPrettyFormat());
        itemList.add(format("Total Flax: %d", totalGained));
        itemList.add(format("Total Flax Hour: %d", stopwatch.getHourlyRate(totalGained)));
        itemList.add(format("Estimated Profit: %d", totalGained * PRICE));
        itemList.add(format("Estimated Profit p/h: %d", stopwatch.getHourlyRate(totalGained * PRICE)));

        graphics.setColor(new Color(61, 61, 61, 100));
        graphics.fillRoundRect(x - 10, y - 10, 300, itemList.size() * 20 + 10, 10, 10);
        graphics.setColor(Color.WHITE);

        y += 10;
        for (String item : itemList) {
            graphics.drawString(item, x, y);
            y += 20;
        }
    }

}
