import ms.aurora.api.Npcs;
import ms.aurora.api.script.LoopScript;
import ms.aurora.api.script.ScriptMetadata;
import ms.aurora.api.wrappers.RSNPC;

import java.awt.*;

/**
 * @author rvbiljouw
 */
@ScriptMetadata(name = "NpcDebug")
public class NpcDebug extends LoopScript {
    @Override
    public int loop() {
        return 1000;
    }

    @Override
    public void draw(Graphics2D g) {
        RSNPC[] npcs = Npcs.getAll();
        for (RSNPC npc : npcs) {
            Point pos = npc.getScreenLocation();
            g.drawString(String.valueOf(npc.getId()), pos.x, pos.y);
        }
    }
}
