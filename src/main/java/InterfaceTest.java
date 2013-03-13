import ms.aurora.api.Widgets;
import ms.aurora.api.script.LoopScript;
import ms.aurora.api.script.ScriptMetadata;
import ms.aurora.api.wrappers.RSWidget;

import java.awt.*;

/**
 * @author rvbiljouw
 */
@ScriptMetadata(name = "dix", contributors = {"dix"}, version = 1.0, description = "")
public class InterfaceTest extends LoopScript {
    @Override
    public int loop() {
        return 1000;
    }

    @Override
    public void draw(Graphics2D gfx) {
        RSWidget inventory = Widgets.getWidget(149, 0);
        if(inventory != null) {
            gfx.setColor(Color.CYAN);
            gfx.fillRect(inventory.getX(), inventory.getY(), 100, 100);
            gfx.drawString(inventory.getX() + " " + inventory.getY() + " " + inventory.getWidth() + " " + inventory.getHeight(), 10, 10);
        } else {
            gfx.drawString("Can't find inventory", 20, 40);
        }
    }
}
