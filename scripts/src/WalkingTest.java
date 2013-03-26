import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.api.wrappers.RSTilePath;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * Date: 26/03/13
 * Time: 09:24
 *
 * @author A_C/Cov
 */
@ScriptManifest(name = "Walking Test", shortDescription = "Walking Test", author = "A_C", version = 1.0)
public class WalkingTest extends Script implements PaintListener {


    @Override
    public void onRepaint(Graphics graphics) {

    }

    @Override
    public int tick() {
        RSTilePath path = walking.createPath(new RSTile[] {
                new RSTile(3259, 3227), new RSTile(3259, 3232), new RSTile(3259, 3237),
                new RSTile(3258, 3242), new RSTile(3257, 3248), new RSTile(3252, 3253),
                new RSTile(3251, 3258)
        });

        if (!players.getLocal().isMoving()) {
            if (path.step()) {
                return -1;
            }
        }
        return 0;
    }
}