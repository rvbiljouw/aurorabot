package ms.aurora.gui;

import ms.aurora.core.Session;
import ms.aurora.loader.AppletLoader;
import ms.aurora.loader.AppletLoader.CompletionListener;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.applet.Applet;

public final class ApplicationController {
    private final Logger logger = Logger.getLogger(ApplicationController.class);
    private final BrowserPanel welcomePanel = new BrowserPanel(
            "http://www.aurora.ms");
    private final ApplicationGUI gui;

    public ApplicationController() {
        this.gui = new ApplicationGUI(this);
        this.initialized();
    }

    /**
     * Called when the frame has been initialized.
     */
    public void initialized() {
        gui.getTabbedPane().addTab("Welcome!", welcomePanel);
        gui.setVisible(true);
        logger.info("Initialized GUI");
    }

    public void onNewClient() {
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
                        gui.getTabbedPane().addTab(tabName, applet);
                        logger.info("Initialized " + tabName);
                    }
                });
            }
        });
        loader.start();
    }

    public void onSelectScript() {

    }

    private Applet getSelectedApplet() {
        Object component = gui.getTabbedPane().getSelectedComponent();
        if (component instanceof Applet) {
            return (Applet) component;
        }
        return null;
    }

    public void onTabChanged() {

    }

    private static final String TAB_PREFIX = "Session #";
}
