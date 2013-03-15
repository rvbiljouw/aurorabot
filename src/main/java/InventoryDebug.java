import ms.aurora.api.script.LoopScript;
import ms.aurora.api.script.ScriptMetadata;
import ms.aurora.api.tabs.Inventory;

import java.awt.*;

/**
 * @author rvbiljouw
 */
@ScriptMetadata(name="InventoryDebug")
public class InventoryDebug extends LoopScript {
    @Override
    public int loop() {
        return 4134;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void draw(Graphics2D g) {
        for(Inventory.Item item : Inventory.getAll()) {
            Point loc = item.getLocation();
            g.drawString(String.valueOf(item.getId()), loc.x, loc.y);
        }
    }
}
