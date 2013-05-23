package ms.aurora.core;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ms.aurora.gui.widget.AppletWidget;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User interface management for sessions.
 * @author rvbiljouw
 */
public class SessionUI {
    private final CopyOnWriteArrayList<MenuItem> pluginMenu = new CopyOnWriteArrayList<MenuItem>();

    private final AppletWidget container;
    private final Session session;

    public SessionUI(Session session, AppletWidget container) {
        this.session = session;
        this.container = container;
    }

    public void registerMenu(final Menu menu) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pluginMenu.add(menu);
            }
        });
    }

    public void deregisterMenu(final Menu menu) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pluginMenu.remove(menu);
            }
        });
    }

    public void update() {
        getContainer().getTab().setText(session.getAccount() != null ?
                session.getAccount().getUsername() : "No account");
    }

    public AppletWidget getContainer() {
        return container;
    }

    public synchronized CopyOnWriteArrayList<MenuItem> getPluginMenu() {
        return pluginMenu;
    }

}
