package ms.aurora;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import ms.aurora.core.model.AbstractModel;
import ms.aurora.core.model.Account;
import ms.aurora.core.model.PluginConfig;
import ms.aurora.core.model.Property;
import ms.aurora.sdn.net.api.Versioning;

import static com.avaje.ebean.EbeanServerFactory.create;

/**
 * @author rvbiljouw
 */
public class Database {
    private static final Class<?>[] BEAN_CLASSES = {
            AbstractModel.class, Account.class,
            PluginConfig.class, ms.aurora.core.model.Source.class,
            Property.class
    };

    public static void init() {
        ServerConfig cfg = getConfig();
        if (!test(cfg)) {
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
        dataSource.setUrl("jdbc:h2:~/.aurora.db;Recover=1");
        config.setDataSourceConfig(dataSource);
        config.setDefaultServer(true);
        config.setRegister(true);
        return config;
    }

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
