package ms.aurora.gui.widget;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import ms.aurora.Application;
import ms.aurora.api.util.Utilities;
import ms.aurora.core.Session;
import ms.aurora.gui.ApplicationGUI;

import javax.swing.*;
import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import static ms.aurora.core.SessionRepository.get;

/**
 * @author rvbiljouw
 */
public class AppletWidget extends AnchorPane implements ChangeListener<Boolean> {
    private final Tab tab = new Tab();
    private final ApplicationGUI parent;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane appletPane;


    private Applet applet;

    public AppletWidget(final ApplicationGUI parent) {
        this.parent = parent;
        this.loadFace();
        tab().setContent(this);
        tab().setClosable(true);
        tab().setText("Loading...");
        tab().selectedProperty().addListener(this);
    }

    private void loadFace() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppletWidget.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void initialize() {
        assert appletPane != null : "fx:id=\"appletPane\" was not injected: check your FXML file 'AppletWidget.fxml'.";
    }

    public void setApplet(final Applet applet) {
        Application.registerApplet(applet);
        toggleVisibility(tab().isSelected(), applet);
        this.applet = applet;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Utilities.sleepNoException(100);
                toggleVisibility(tab().isSelected(), applet);
            }
        });
    }

    private int calculateRelX() {
        int x = 0;
        Parent obj = this;
        while (obj != null) {
            x += obj.getLayoutX();
            obj = obj.getParent();
        }
        return x;
    }

    private int calculateRelY() {
        int y = 0;
        Parent obj = this;
        while (obj != null) {
            y += obj.getLayoutY();
            obj = obj.getParent();
        }
        return y;
    }

    public Applet getApplet() {
        return applet;
    }

    public Tab tab() {
        return tab;
    }

    private void toggleVisibility(Boolean observableValue, final Applet applet) {
        if (applet == null) return;

        int relX = calculateRelX();
        int relY = calculateRelY();
        if (observableValue) {
            applet.setBounds(relX, relY, applet.getWidth(),
                    applet.getHeight());
            applet.setSize(765, 503);
            applet.setVisible(true);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tab().setText("Session " + applet.hashCode());
                }
            });
        } else {
            applet.setBounds(relX - 1000, relY,
                    applet.getWidth(), applet.getHeight());
            applet.setSize(765, 503);
            applet.setVisible(false);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tab().setText("[ Session " + applet.hashCode() + " ]");
                }
            });
        }
    }

    public void onMenuOpening() {
        if (applet != null) {
            final Session session = get(applet.hashCode());
            CopyOnWriteArrayList<MenuItem> pluginMenu = session.getPluginMenu();
            ObservableList<MenuItem> items = FXCollections.observableArrayList();
            for (MenuItem pluginItem : pluginMenu) {
                items.add(pluginItem);
            }
            parent.getPluginsMenu().getItems().clear();
            parent.getPluginsMenu().getItems().add(parent.getPluginOverview());
            parent.getPluginsMenu().getItems().addAll(items);
        }
    }
}
