package ms.aurora;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import ms.aurora.gui.ApplicationGUI;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public final class Application {
    private static Logger logger = Logger.getLogger(Application.class);
    private static JFrame appWindow;

    public static void main(final String[] args) {
        appWindow = new JFrame("Aurora!");
        final JFXPanel panel = new JFXPanel();
        panel.setSize(765, 600);
        appWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        appWindow.setContentPane(panel);
        appWindow.setSize(765, 600);
        appWindow.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene scene = new Scene(new ApplicationGUI());
                panel.setScene(scene);
            }
        });
    }

    public static void registerComponent(Component component) {
        appWindow.add(component);
    }
}
