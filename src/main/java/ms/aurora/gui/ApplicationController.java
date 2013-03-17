package ms.aurora.gui;

import ms.aurora.core.Session;
import ms.aurora.gui.plugin.PluginOverview;
import ms.aurora.gui.script.ScriptOverview;
import ms.aurora.loader.AppletLoader;
import ms.aurora.loader.AppletLoader.CompletionListener;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

public final class ApplicationController {
    private final static Logger logger = Logger.getLogger(ApplicationController.class);
    private final static ApplicationGUI gui = new ApplicationGUI();

    public static void startApplication() {
        gui.setVisible(true);
        logger.info("Initialized GUI");
    }

    public static void onNewClient() {
        AppletLoader loader = new AppletLoader();
        loader.setCompletionListener(new CompletionListener() {
            @Override
            public void onCompletion(final Applet applet) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Session session = new Session(applet);
                        Thread init = new Thread(session);
                        init.start();

                        String tabName = TAB_PREFIX + applet.hashCode();
                        JPanel faggot = new JPanel();
                        faggot.setBounds(0, 0, 768, 503);
                        faggot.add(applet, BorderLayout.CENTER);
                        gui.getTabbedPane().addTab(tabName, faggot);
                        logger.info("Initialized " + tabName);
                    }
                });
            }
        });
        loader.start();
    }

    public static void onSelectScript() {
    	ScriptOverview overview = new ScriptOverview();
    	overview.setVisible(true);
    }

    public static void onPluginOverview() {
        PluginOverview overview = new PluginOverview();
        overview.setVisible(true);
    }

    public static void addToolsEntry(JMenu menu) {
        gui.getTools().add(menu);
        gui.getTools().repaint();
        System.out.println("FUCK YOU");
    }

    private static final String TAB_PREFIX = "Session #";
}
