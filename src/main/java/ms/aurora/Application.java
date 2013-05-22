package ms.aurora;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import ms.aurora.core.model.*;
import ms.aurora.event.GlobalEventQueue;
import ms.aurora.gui.Main;
import ms.aurora.gui.Messages;
import ms.aurora.gui.sdn.LoginWindow;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.api.Hooks;
import ms.aurora.sdn.net.api.Maps;
import ms.aurora.sdn.net.api.Versioning;
import ms.aurora.security.DefaultSecurityManager;
import org.apache.log4j.Logger;

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
    public static final double VERSION = 12;
    private static final Object initialisation_lock = new Object();

    public static LoginWindow LOGIN_WINDOW;
    public static JFrame mainFrame;

    public static void main(String[] args) {
        System.setSecurityManager(new DefaultSecurityManager());
        getDefaultToolkit().getSystemEventQueue().push(new GlobalEventQueue());
        boot();
    }

    public static void boot() {
        try {
            loadDatabase();
            for(Class<?> beanClass : BEAN_CLASSES) {
                if(AbstractModel.class.isAssignableFrom(beanClass)
                        && !beanClass.equals(Account.class)) {
                    AbstractModel.test(beanClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            newDatabase();
        }

        new JFXPanel();
        SDNConnection.getInstance().start();
        Versioning.checkForUpdates();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Account.init();
                LOGIN_WINDOW = new LoginWindow();
                LOGIN_WINDOW.display();
            }
        });
    }

    public static void showStage() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame = new JFrame(MessageFormat.format(Messages.getString("mainWindow.title"), VERSION));
                mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                final JFXPanel panel = new JFXPanel();
                mainFrame.setContentPane(panel);
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
                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                        int centerX = (toolkit.getScreenSize().width / 2) - (mainFrame.getWidth() / 2);
                        int centerY = (toolkit.getScreenSize().height / 2) - (mainFrame.getHeight() / 2);
                        mainFrame.setLocation(centerX, centerY);
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

    private static void newDatabase() {
        ServerConfig config = new ServerConfig();
        config.setName("default");
        config.addJar(Versioning.PATH);
        for (Class<?> clazz : BEAN_CLASSES) {
            config.addClass(clazz);
        }

        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDriver("org.h2.Driver");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:h2:~/.aurora.db;Recover=1");

        config.setDataSourceConfig(dataSource);
        config.setDdlGenerate(true);
        config.setDdlRun(true);

        config.setDefaultServer(true);
        config.setRegister(true);
        EbeanServer server = EbeanServerFactory.create(config);
        server.runCacheWarming();

        logger.info("Database initialised for the first time!");
        logger.info("Next time we will use the properties file.");
    }

    private static void loadDatabase() {
        ServerConfig config = new ServerConfig();
        config.setName("default");
        config.addJar(Versioning.PATH);
        for (Class<?> clazz : BEAN_CLASSES) {
            config.addClass(clazz);
        }

        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDriver("org.h2.Driver");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:h2:~/.aurora.db;Recover=1");

        config.setDataSourceConfig(dataSource);
        config.setDdlGenerate(false);
        config.setDdlRun(false);

        config.setDefaultServer(true);
        config.setRegister(true);
        EbeanServer server = EbeanServerFactory.create(config);
        server.runCacheWarming();

        logger.info("Database initialised for the first time!");
        logger.info("Next time we will use the properties file.");
    }

    private static Class<?>[] BEAN_CLASSES = {
            AbstractModel.class, Account.class,
            PluginConfig.class, ms.aurora.core.model.Source.class,
            Property.class
    };
}
