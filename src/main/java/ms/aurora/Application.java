package ms.aurora;

import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ms.aurora.core.model.Account;
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
    private static Stage mainStage;

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
        mainStage = stage;
        LOGIN_WINDOW = new LoginWindow();
        LOGIN_WINDOW.display();
        Versioning.checkForUpdates();
    }

    public static void showStage() {
        Account.init();

        mainStage.setTitle("Aurora - Automation Toolkit");
        mainStage.setResizable(false);
        Scene scene = new Scene(new ApplicationGUI(), 765, 590);
        scene.getStylesheets().add("soft-responsive.css");
        mainStage.setScene(scene);
        mainStage.centerOnScreen();
        mainStage.show();

        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }
}
