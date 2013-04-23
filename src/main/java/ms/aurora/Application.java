package ms.aurora;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import ms.aurora.event.GlobalEventQueue;
import ms.aurora.gui.ApplicationGUI;
import ms.aurora.gui.swing.LoginWindow;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.security.DefaultSecurityManager;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

import static java.awt.Toolkit.getDefaultToolkit;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Application entry point
 *
 * @author Rick
 */
public final class Application {
    public static final Logger logger = Logger.getLogger(Application.class);
    public static final LoginWindow LOGIN_WINDOW = new LoginWindow();
    private static JFrame appWindow;

    public static void main(String[] args) {
        SDNConnection.getInstance().start();
        LOGIN_WINDOW.setVisible(true);
    }

    public static void init() {
        System.setSecurityManager(new DefaultSecurityManager());
        getDefaultToolkit().getSystemEventQueue().push(new GlobalEventQueue());
        final JFXPanel panel = new JFXPanel();
        appWindow = new JFrame("Aurora - Automation Toolkit");
        appWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        appWindow.add(panel, BorderLayout.CENTER);
        appWindow.setVisible(true);
        panel.setPreferredSize(new Dimension(765, 600));
        appWindow.pack();
        appWindow.setSize(appWindow.getWidth() - 10, appWindow.getHeight() - 10);
        appWindow.setResizable(false);
        logger.info("Welcome to Aurora!");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = new Scene(new ApplicationGUI());
                scene.getStylesheets().add("soft-responsive.css");
                panel.setScene(scene);
            }
        });
    }

    /**
     * Adds an applet to our application frame.
     * This is a bit of a hack, the reason we have to do this
     * :q
     * <p/>
     * is because applets don't play nice with JavaFX and when we don't add them
     * to anything they wont even render, so yeah!
     *
     * @param applet Applet
     */
    public static void registerApplet(Applet applet) {
        appWindow.add(applet);
    }
}
