package ms.aurora;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import ms.aurora.event.GlobalEventQueue;
import ms.aurora.gui.ApplicationGUI;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.applet.Applet;

import static java.awt.Toolkit.getDefaultToolkit;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public final class Application {
    private static Logger logger = Logger.getLogger(Application.class);
    private static JFrame appWindow;

    public static void main(final String[] args) {
        getDefaultToolkit().getSystemEventQueue().push(new GlobalEventQueue());

        appWindow = new JFrame("Aurora!");
        final JFXPanel panel = new JFXPanel();
        panel.setSize(768, 620);
        appWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        appWindow.setContentPane(panel);
        appWindow.setSize(768, 620);
        appWindow.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = new Scene(new ApplicationGUI());
                scene.getStylesheets().add("blue.css");
                panel.setScene(scene);
            }
        });
    }

    public static void registerApplet(Applet applet) {
        appWindow.add(applet);
    }
}
