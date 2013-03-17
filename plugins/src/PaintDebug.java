import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author rvbiljouw
 */
@PluginManifest(name = "Paint Debug", author = "rvbiljouw", version = 1.0)
public class PaintDebug extends Plugin {
    private boolean npcPaintActive = false;
    private NpcPaint npcPaint = new NpcPaint();

    @Override
    public void startup() {
        info("Starting NpcPaint");

        JMenu paint = new JMenu("Paint");
        JCheckBoxMenuItem npcs = new JCheckBoxMenuItem("NPCs");
        npcs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!npcPaintActive) {
                    getSession().getPaintManager().register(npcPaint);
                } else {
                    getSession().getPaintManager().deregister(npcPaint);
                }
                npcPaintActive = !npcPaintActive;
            }
        });
        paint.add(npcs);
        registerMenu(paint);
    }

    @Override
    public void execute() {
        info("Started NpcPaint");
    }

    @Override
    public void cleanup() {
        info("Stopping NpcPaint");
    }



}
