package ms.aurora;

import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ms.aurora.event.GlobalEventQueue;
import ms.aurora.gui.ApplicationGUI;
import ms.aurora.gui.sdn.LoginWindow;
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
    public static LoginWindow LOGIN_WINDOW;

    public static void main(String[] args) {
        System.setSecurityManager(new DefaultSecurityManager());
        getDefaultToolkit().getSystemEventQueue().push(new GlobalEventQueue());
        SDNConnection.getInstance().start();
        boot();
    }

    public static void boot() {
        new JFXPanel();
        launch("");
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGIN_WINDOW = new LoginWindow();
        LOGIN_WINDOW.display();
        Versioning.checkForUpdates();
        stage.setTitle("Aurora - Automation Toolkit");
        stage.setResizable(false);
        Scene scene = new Scene(new ApplicationGUI(), 765, 590);
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
