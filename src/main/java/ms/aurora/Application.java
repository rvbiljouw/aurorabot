package ms.aurora;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import ms.aurora.core.model.Account;
import ms.aurora.event.GlobalEventQueue;
import ms.aurora.gui.ApplicationGUI;
import ms.aurora.gui.sdn.LoginWindow;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.api.Hooks;
import ms.aurora.sdn.net.api.Maps;
import ms.aurora.sdn.net.api.Versioning;
import ms.aurora.security.DefaultSecurityManager;
import org.apache.log4j.Logger;

import javax.swing.*;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import static java.awt.Toolkit.getDefaultToolkit;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Application entry point
 *
 * @author Rick
 */
public final class Application {
    public static final ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
    public static final Logger logger = Logger.getLogger(Application.class);
    public static final double VERSION = 8;
    private static final Object initialisation_lock = new Object();

    public static LoginWindow LOGIN_WINDOW;
    public static JFrame mainFrame;

    public static void main(String[] args) {
        System.setSecurityManager(new DefaultSecurityManager());
        getDefaultToolkit().getSystemEventQueue().push(new GlobalEventQueue());
        boot();
    }

    public static void boot() {
        new JFXPanel();
        SDNConnection.getInstance().start();
        Versioning.checkForUpdates();

        LOGIN_WINDOW = new LoginWindow();
        LOGIN_WINDOW.display();
    }

    public static void showStage() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame = new JFrame(MessageFormat.format(resourceBundle.getString("mainWindow.title"), VERSION));
                mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                final JFXPanel panel = new JFXPanel();
                mainFrame.setContentPane(panel);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Account.init();
                        Scene scene = new Scene(new ApplicationGUI());
                        scene.getStylesheets().add(resourceBundle.getString("gui.theme"));
                        panel.setScene(scene);
                        synchronized (initialisation_lock) {
                            initialisation_lock.notifyAll();
                        }
                    }
                });

                try {
                    synchronized (initialisation_lock) {
                        initialisation_lock.wait();
                        String name = System.getProperty("os.name");
                        if (name.toLowerCase().contains("win")) {
                            mainFrame.setSize(765, 620);
                        } else {
                            mainFrame.pack();
                        }
                        mainFrame.setResizable(false);
                        mainFrame.setVisible(true);
                        initialize();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void initialize() {
        Hooks.obtainHooks();
        Maps.obtainMap();
    }
}
