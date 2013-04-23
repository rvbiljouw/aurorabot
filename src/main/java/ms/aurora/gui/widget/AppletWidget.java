package ms.aurora.gui.widget;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import ms.aurora.Application;
import ms.aurora.api.util.Utilities;
import ms.aurora.core.Session;
import ms.aurora.core.SessionRepository;
import ms.aurora.gui.ApplicationGUI;

import javax.swing.*;
import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import static ms.aurora.core.SessionRepository.get;

/**
 * @author Rick
 */
public class AppletWidget extends AnchorPane {
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
        tab().setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Session mySession = SessionRepository.get(applet.hashCode());
                if(mySession != null) {
                    mySession.destroy();
                }
            }
        });
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
        this.applet = applet;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getChildren().clear();
                getChildren().add(new AppletWrapper(applet));
            }
        });
    }

    public Applet getApplet() {
        return applet;
    }

    public Tab tab() {
        return tab;
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
