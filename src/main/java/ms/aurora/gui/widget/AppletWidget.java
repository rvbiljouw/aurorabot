package ms.aurora.gui.widget;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import ms.aurora.Application;
import ms.aurora.api.util.Utilities;
import ms.aurora.core.Repository;
import ms.aurora.core.Session;
import ms.aurora.gui.Main;
import ms.aurora.gui.Messages;

import javax.swing.*;
import java.applet.Applet;
import java.util.concurrent.CopyOnWriteArrayList;

import static ms.aurora.core.Repository.get;
import static ms.aurora.gui.util.FXUtils.load;

/**
 * @author Rick
 */
public class AppletWidget extends AnchorPane implements ChangeListener<Boolean> {
    private static final String FXML = "AppletWidget.fxml";
    private static final int WIDTH = 765;
    private static final int HEIGHT = 503;
    private static final int OOS_X = 1000;

    private final Tab tab = new Tab();
    private final Main parent;
    private Applet applet;

    public AppletWidget(Main parent) {
        load(getClass().getResource(FXML), this);
        this.parent = parent;
    }

    @FXML
    void initialize() {
        tab.setContent(this);
        tab.setClosable(false);
        tab.setText(Messages.getString("appletWidget.noAccount"));
        tab.selectedProperty().addListener(this);
        tab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Session mySession = Repository.get(applet.hashCode());
                if (mySession != null) {
                    mySession.destroy();
                }
            }
        });
    }

    @Override
    public void changed(ObservableValue prop, Boolean old, Boolean newValue) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Utilities.sleepNoException(100);
                update();
            }
        });
    }

    /**
     * Updates the applet widget.
     */
    public void update() {
        if (applet == null) return;
        int relX = getRelativeX();
        int relY = getRelativeY();
        if (relX < 0 || relY < 0) {
            return;
        }

        setInternal(relX + (tab.isSelected() ? 0 : OOS_X), relY);
    }

    /**
     * Sets the coordinates of the wrapper applets
     * Goes out of bounds if the tab is not visible.
     * @param relX Relative X
     * @param relY Relative Y
     */
    private void setInternal(int relX, int relY) {
        applet.setBounds(relX, relY, applet.getWidth(),
                applet.getHeight());
        applet.setSize(WIDTH, HEIGHT);
        applet.setVisible(true);
    }

    /**
     * Updates the plugin menu when it is opened.
     */
    public void onMenuOpening() {
        if (applet != null) {
            final Session session = get(applet.hashCode());
            CopyOnWriteArrayList<MenuItem> pluginMenu = session.getUI().getPluginMenu();
            ObservableList<MenuItem> items = FXCollections.observableArrayList();
            for (MenuItem pluginItem : pluginMenu) {
                items.add(pluginItem);
            }
            parent.getPluginsMenu().getItems().clear();
            parent.getPluginsMenu().getItems().add(parent.getPluginOverview());
            parent.getPluginsMenu().getItems().addAll(items);
        }
    }

    /**
     * Calculates the relative X of this widget.
     * @return relative X
     */
    private int getRelativeX() {
        int x = 0;
        Parent obj = this;
        while (obj != null) {
            x += obj.getLayoutX();
            obj = obj.getParent();
        }
        return x;
    }

    /**
     * Calculates the relative Y of this widget.
     * @return relative Y
     */
    private int getRelativeY() {
        int y = 0;
        Parent obj = this;
        while (obj != null) {
            y += obj.getLayoutY();
            obj = obj.getParent();
        }
        return y;
    }

    /**
     * Returns the wrapped applet.
     * @return wrapped applet.
     */
    public Applet getApplet() {
        return applet;
    }

    /**
     * Sets the wrapped applet
     * @param applet applet
     */
    public void setApplet(final Applet applet) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Application.mainFrame.add(applet);
            }
        });

        this.applet = applet;

        tab.setClosable(true);
        update();
    }

    public Tab getTab() {
        return tab;
    }
}