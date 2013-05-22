package ms.aurora.api;

import ms.aurora.api.script.ScriptState;
import ms.aurora.core.Repository;
import ms.aurora.core.Session;
import ms.aurora.rt3.Client;
import org.apache.log4j.Logger;

import static java.lang.Thread.currentThread;
import static ms.aurora.api.methods.Widgets.getWidget;

/**
 * @author Rick
 */
public class Context {
    private static final Logger logger = Logger.getLogger(Context.class);
    private static final ThreadLocal<Session> session = new ThreadLocal<>();

    public static Session getSession() {
        if(session.get() == null) {
            Session group = Repository.get(currentThread().getThreadGroup());
            if(group != null) {
                session.set(group);
            }
        }
        return session.get();
    }

    public static Client getClient() {
        return (Client) getSession().getApplet();
    }

    public static void setProperty(String key, String value) {
        getSession().getProperties().setProperty(key, value);
    }

    public static String getProperty(String key) {
        return getSession().getProperties().getProperty(key);
    }

    public static boolean isActive() {
        return getSession().getScriptManager().getState() != ScriptState.STOP;
    }

    public static boolean isLoggedIn() {
        return getClient().getLoginIndex() == 30 && getWidget(378, 6) == null;
    }

    public static void invokeLater(Runnable runnable) {
        Thread thread = new Thread(getSession().getThreadGroup(), runnable);
        thread.start();
    }
}
