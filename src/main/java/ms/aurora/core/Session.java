package ms.aurora.core;

import ms.aurora.api.methods.web.model.World;
import ms.aurora.core.model.Account;
import ms.aurora.core.script.PluginManager;
import ms.aurora.core.script.ScriptManager;
import ms.aurora.event.PaintManager;
import ms.aurora.gui.widget.AppletWidget;
import ms.aurora.loader.ClientWrapper;

import java.applet.Applet;

/**
 * @author Rick
 */
public final class Session implements Runnable {
    private final SessionProperties properties = new SessionProperties();
    private final PaintManager paintManager = new PaintManager();
    private final ClientWrapper wrapper = new ClientWrapper();
    private final ThreadGroup threadGroup;
    private final SessionUI ui;
    private ScriptManager scriptManager;
    private PluginManager pluginManager;
    private Account account;

    public Session(ThreadGroup threadGroup, AppletWidget container) {
        this.ui = new SessionUI(this, container);
        this.threadGroup = threadGroup;
    }

    @Override
    public void run() {
        wrapper.start();
        if (wrapper.getApplet() != null) {
            scriptManager = new ScriptManager(this);
            pluginManager = new PluginManager(this);
            ui.getContainer().setApplet(wrapper.getApplet());
            Repository.set(wrapper.getApplet().hashCode(), this);
            pluginManager.refresh();
        }
    }

    public void destroy() {
        try {
            scriptManager.stop();
            pluginManager.stop();
            wrapper.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SessionUI getUI() {
        return ui;
    }

    public Applet getApplet() {
        return wrapper.getApplet();
    }

    public SessionProperties getProperties() {
        return properties;
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public PaintManager getPaintManager() {
        return paintManager;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
        this.ui.update();
    }

    public void setWorld(World world) {
        wrapper.setWorld(world);
    }
}
