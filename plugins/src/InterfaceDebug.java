import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author tobiewarburton
 */
@PluginManifest(name = "Interface Debug", author = "tobiewarburton", version = 1.0)
public class InterfaceDebug extends Plugin {
    private InterfaceExplorer explorer = new InterfaceExplorer();

    private boolean interfacePaintActive = false;


    private JMenu menu;

    @Override
    public void startup() {
        menu = new JMenu("Interfaces");

        JCheckBoxMenuItem interfaces = new JCheckBoxMenuItem("Toggle Interface Explorer");
        interfaces.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!interfacePaintActive) {
                    getSession().getPaintManager().register(explorer);
                    explorer.toggle();
                } else {
                    getSession().getPaintManager().deregister(explorer);
                    explorer.toggle();
                }
                interfacePaintActive = !interfacePaintActive;
            }
        });
        menu.add(interfaces);

        getSession().registerMenu(menu);
    }

    @Override
    public void execute() {
    }

    @Override
    public void cleanup() {
        getSession().deregisterMenu(menu);
    }



}
