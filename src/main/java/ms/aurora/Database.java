package ms.aurora;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import ms.aurora.core.model.AbstractModel;
import ms.aurora.core.model.Account;
import ms.aurora.core.model.PluginConfig;
import ms.aurora.core.model.Property;
import ms.aurora.sdn.net.api.Versioning;
import org.h2.tools.Server;

import java.sql.SQLException;

import static com.avaje.ebean.EbeanServerFactory.create;

/**
 * Database is responsible for maintaining connectivity
 * to the h2 database, as well as recreating the schema
 * in case it goes corrupt / m.i.a.
 * @author rvbiljouw
 */
public class Database {
    /**
     * An array of bean classes that are (or should be) registered
     * with the database server.
     */
    private static final Class<?>[] BEAN_CLASSES = {
            AbstractModel.class, Account.class,
            PluginConfig.class, ms.aurora.core.model.Source.class,
            Property.class
    };

    public static void init() {
        try {
            Server.createTcpServer().start();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ServerConfig cfg = getConfig();
        if (!test(cfg)) {
            // Database was corrupt or not present,
            // regenerate the schema.
            cfg.setDdlGenerate(true);
            cfg.setDdlRun(true);
        }
        EbeanServer server = create(cfg);
        server.runCacheWarming();
    }

    private static ServerConfig getConfig() {
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
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/.aurora.db;Recover=1");
        config.setDataSourceConfig(dataSource);
        config.setDefaultServer(true);
        config.setRegister(true);
        return config;
    }

    /**
     * Tests a server configuration to see if all
     * beans were properly registered. If they weren't
     * it will throw an exception allowing us to
     * re-instantiate the database model.
     * @param cfg A server configuration
     * @return true when successful
     */
    public static boolean test(ServerConfig cfg) {
        try {
            EbeanServer server = create(cfg);
            server.runCacheWarming();

            for (Class<?> beanClass : BEAN_CLASSES) {
                if (AbstractModel.class.isAssignableFrom(beanClass)
                        && !beanClass.equals(Account.class)) {
                    AbstractModel.test(beanClass);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
