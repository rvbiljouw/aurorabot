package ms.aurora.gui.widget;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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
import ms.aurora.loader.AppletLoader;

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
public class AppletWidget extends AnchorPane implements AppletLoader.CompletionListener, ChangeListener<Boolean>, Session.UpdateListener {
    private final Tab tab = new Tab();
    private final ApplicationGUI parent;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane appletPane;


    private Applet applet;

    public AppletWidget(ApplicationGUI parent) {
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

    @Override
    public void onCompletion(final Applet applet) {
        Application.registerApplet(applet);
        toggleVisibility(tab().isSelected(), applet);
        this.applet = applet;

        setPluginMenu();
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Utilities.sleepNoException(100);
                toggleVisibility(tab().isSelected(), applet);

                setPluginMenu();
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

    private final ListChangeListener<MenuItem> menuChangeListener = new ListChangeListener<MenuItem>() {
        @Override
        public void onChanged(Change<? extends MenuItem> change) {
            setPluginMenu();
        }
    };

    private void setPluginMenu() {
        final Session session = get(applet.hashCode());
        if (session != null) {
            session.setUpdateListener(this);
            onUpdate(); // Force the first update.
        }
    }

    @Override
    public synchronized void onUpdate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                final Session session = get(applet.hashCode());
                CopyOnWriteArrayList<MenuItem> pluginMenu = session.getPluginMenu();
                if(tab().isSelected()) {
                    parent.getPluginsMenu().getItems().addAll(pluginMenu.toArray(new MenuItem[pluginMenu.size()]));
                } else {
                    parent.getPluginsMenu().getItems().removeAll(pluginMenu.toArray(new MenuItem[pluginMenu.size()]));
                }
            }
        });
    }
}
