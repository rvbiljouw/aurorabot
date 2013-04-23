package ms.aurora;

import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ms.aurora.event.GlobalEventQueue;
import ms.aurora.gui.ApplicationGUI;
import ms.aurora.gui.swing.LoginWindow;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.api.Versioning;
import ms.aurora.security.DefaultSecurityManager;
import org.apache.log4j.Logger;

import static java.awt.Toolkit.getDefaultToolkit;

/**
 * Application entry point
 *
 * @author Rick
 */
public final class Application extends javafx.application.Application {
    public static final Logger logger = Logger.getLogger(Application.class);
    public static final LoginWindow LOGIN_WINDOW = new LoginWindow();

    public static void main(String[] args) {
        System.setSecurityManager(new DefaultSecurityManager());
        getDefaultToolkit().getSystemEventQueue().push(new GlobalEventQueue());
        SDNConnection.getInstance().start();
        LOGIN_WINDOW.setVisible(true);
    }

    public static void boot() {
        new JFXPanel();
        Versioning.checkForUpdates();
        launch("");
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Aurora - Automation Toolkit");
        stage.setMinWidth(765);
        stage.setMinHeight(610);
        stage.setResizable(false);

        Scene scene = new Scene(new ApplicationGUI());
        scene.getStylesheets().add("soft-responsive.css");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }
}
