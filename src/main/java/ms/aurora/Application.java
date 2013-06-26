package ms.aurora;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import ms.aurora.api.event.GlobalEventQueue;
import ms.aurora.core.model.Account;
import ms.aurora.gui.Main;
import ms.aurora.gui.Messages;
import ms.aurora.gui.sdn.LoginWindow;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.api.ClientData;
import ms.aurora.sdn.net.api.Versioning;
import ms.aurora.security.DefaultSecurityManager;
import org.apache.log4j.Logger;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

import static java.awt.Toolkit.getDefaultToolkit;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Application entry point
 *
 * @author Rick
 */
public final class Application {
    public static final Logger logger = Logger.getLogger(Application.class);
    private static final Object initialisation_lock = new Object();

    public static final double VERSION = 1;
    public static LoginWindow LOGIN_WINDOW;
    public static JFrame mainFrame;

    public static void main(String[] args) {
        System.setSecurityManager(new DefaultSecurityManager());
        getDefaultToolkit().getSystemEventQueue().push(new GlobalEventQueue());

        Database.init();
        while(!SDNConnection.getInstance().start()) {
            SDNConnection.getInstance().start();
        }
        Versioning.checkForUpdates();

        new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Account.init();
                LOGIN_WINDOW = new LoginWindow();
                LOGIN_WINDOW.showAndWait();
            }
        });
    }

    public static void showStage() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    mainFrame = new JFrame(MessageFormat.format(Messages.getString("mainWindow.title"), VERSION));
                    mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    JFXPanel panel = new JFXPanel();
                    mainFrame.setContentPane(panel);
                    initializeScene(panel);

                    synchronized (initialisation_lock) {
                        initialisation_lock.wait();
                        showFrame();
                        onReady();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void initializeScene(final JFXPanel panel) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = new Scene(new Main());
                scene.getStylesheets().add(Messages.getString("gui.theme"));
                panel.setScene(scene);
                synchronized (initialisation_lock) {
                    initialisation_lock.notifyAll();
                }
            }
        });
    }

    private static void showFrame() {
        mainFrame.pack();
        mainFrame.setResizable(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (mainFrame.getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (mainFrame.getHeight() / 2);
        mainFrame.setLocation(centerX, centerY);
        mainFrame.setVisible(true);
    }

    private static void onReady() {
        ClientData.obtainData();
    }
}
