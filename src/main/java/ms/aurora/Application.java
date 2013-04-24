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

import java.io.*;
import java.net.URLDecoder;

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
        if (args.length == 0) {
            delegate();
        } else {
            System.setSecurityManager(new DefaultSecurityManager());
            getDefaultToolkit().getSystemEventQueue().push(new GlobalEventQueue());
            SDNConnection.getInstance().start();
            boot();
        }
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
        mainStage = stage;
    }

    public static void showStage() {
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


    private static void delegate() {
        String jfxRt = System.getProperty("java.home") + File.separator + "lib" + File.separator + "jfxrt.jar";
        if (jfxRt.contains(" ")) {
            jfxRt = "\"" + jfxRt + "\"";
        }

        String path = Application.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            if (decodedPath.contains(" ")) {
                decodedPath = "\"" + decodedPath + "\"";
            }

            String seperator = System.getProperty("os.name").contains("Win") ? ";" : ":";
            String classpath = "." + seperator;
            for (String item : System.getProperty("java.class.path").split(seperator)) {
                if (item.toLowerCase().contains("idea")) continue;

                if (item.contains(" ")) {
                    classpath += "\"" + item + "\"" + seperator;
                } else {
                    classpath += item + seperator;
                }
            }

            String command = "java -cp " + classpath + seperator + decodedPath + seperator + jfxRt + " ms.aurora.Application start";
            final Process p = Runtime.getRuntime().exec(command);
            Thread input = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(p.getInputStream()));
                        String buf = "";
                        while ((buf = reader.readLine()) != null) {
                            System.out.println(buf);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Thread output = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(p.getErrorStream()));
                        String buf = "";
                        while ((buf = reader.readLine()) != null) {
                            System.out.println(buf);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            input.start();
            output.start();
            p.waitFor();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
