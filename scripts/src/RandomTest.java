import ms.aurora.api.methods.tabs.Options;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * @author tobiewarburton
 */
@ScriptManifest(name = "randomer",
        author = "warb0",
        version = 0.1,
        shortDescription = "test dem randoms")
public class RandomTest extends Script implements PaintListener {
    int option;

    @Override
    public int tick() {
        option = (Options.get(312) >> 24) & 0xff;
        info("wut: " + option);
        return 5000;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onRepaint(Graphics graphics) {
        graphics.drawString("lol: " + option, 20, 20);
    }
}
