package ms.aurora.gui;

import ms.aurora.api.script.Script;
import ms.aurora.core.Session;
import ms.aurora.core.SessionRepository;
import ms.aurora.gui.plugin.PluginOverview;
import ms.aurora.gui.script.ScriptOverview;
import ms.aurora.loader.AppletLoader;
import ms.aurora.loader.AppletLoader.CompletionListener;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.applet.Applet;
import java.awt.*;

public final class ApplicationController {
    private final static Logger logger = Logger.getLogger(ApplicationController.class);
    private final static ApplicationGUI gui = new ApplicationGUI();

    public static void startApplication() {
        gui.setVisible(true);
        logger.info("Initialized GUI");

        gui.getTabbedPane().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Applet applet = getSelectedApplet();
                if (applet != null) {
                    Session session = SessionRepository.get(applet.hashCode());
                    if (session != null) {
                        gui.setTools(session.getTools());
                        logger.info("Switched to session " + session.getApplet().hashCode());
                    }
                }
            }

        });
    }

    public static void onScriptOverview() {
        ScriptOverview overview = new ScriptOverview();
        overview.setVisible(true);
    }

    public static void onPluginOverview() {
        PluginOverview overview = new PluginOverview();
        overview.setVisible(true);
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
                        init.run();

                        String tabName = TAB_PREFIX + applet.hashCode();
                        JPanel faggot = new JPanel();
                        faggot.setBounds(0, 0, applet.getWidth(), applet.getHeight());
                        faggot.add(applet, BorderLayout.CENTER);
                        gui.getTabbedPane().addTab(tabName, faggot);
                        logger.info("Initialized " + tabName);
                    }
                });
            }
        });
        loader.start();
    }

    public static void runScript(Script script) {
        Applet applet = getSelectedApplet();
        if (applet != null) {
            Session session = SessionRepository.get(applet.hashCode());
            if (session != null) {
                session.getScriptManager().start(script);
                System.out.println("started script lolol");
            } else {
                System.out.println("wot rofl");
            }
        } else {
            System.out.println("No applet selected..");
        }
    }

    public static Applet getSelectedApplet() {
        JTabbedPane tabs = gui.getTabbedPane();

        if (tabs.getSelectedComponent() instanceof JPanel) {
            JPanel panel = (JPanel) tabs.getSelectedComponent();
            for (Component component : panel.getComponents()) {
                if (component instanceof Applet) {
                    return (Applet) component;
                }
            }
        }
        return null;
    }

    private static final String TAB_PREFIX = "Session #";
}
