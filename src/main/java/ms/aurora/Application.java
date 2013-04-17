package ms.aurora;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import ms.aurora.event.GlobalEventQueue;
import ms.aurora.gui.ApplicationGUI;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.security.DefaultSecurityManager;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.io.*;
import java.net.URLDecoder;

import static java.awt.Toolkit.getDefaultToolkit;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Application entry point
 *
 * @author Rick
 */
public final class Application {
    public static String JAVA_HOME = System.getProperty("java.home");
    public static Logger logger = Logger.getLogger(Application.class);
    public static SDNConnection connection;
    private static JFrame appWindow;

    public static void main(final String[] args) {
        if (args.length == 0) {
            delegate();
        } else {
            //new Login().setVisible(true);
            init();
        }
    }

    public static void init() {
        System.setSecurityManager(new DefaultSecurityManager());
        getDefaultToolkit().getSystemEventQueue().push(new GlobalEventQueue());
        final JFXPanel panel = new JFXPanel();
        appWindow = new JFrame("Aurora - Automation Toolkit");
        panel.setSize(768, 620);
        appWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        appWindow.setContentPane(panel);
        appWindow.setSize(768, 620);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        appWindow.setLocation((int) (dimension.getWidth() / 2 - (768 / 2)),
                (int) (dimension.getHeight() / 2 - (620 / 2)));
        appWindow.setVisible(true);
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

    private static void delegate() {
        String jfxRt = Application.JAVA_HOME + File.separator + "lib" + File.separator + "jfxrt.jar";
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
