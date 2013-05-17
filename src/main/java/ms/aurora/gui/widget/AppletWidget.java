package ms.aurora.gui.widget;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import ms.aurora.Application;
import ms.aurora.Messages;
import ms.aurora.api.util.Utilities;
import ms.aurora.core.Repository;
import ms.aurora.core.Session;
import ms.aurora.gui.ApplicationGUI;

import javax.swing.*;
import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ms.aurora.core.Repository.get;

/**
 * @author Rick
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
        getTab().setContent(this);
        getTab().setClosable(false);
        getTab().setText(Messages.getString("appletWidget.noAccount"));
        getTab().selectedProperty().addListener(this);
        getTab().setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Session mySession = Repository.get(applet.hashCode());
                if (mySession != null) {
                    // TODO
                    //mySession.
                }
            }
        });
    }

    private void loadFace() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppletWidget.fxml"), Messages.getBundle());

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
        if(this.applet != null) {
            Application.mainFrame.remove(this.applet);
        }
        Application.mainFrame.add(applet);
        this.applet = applet;
        tab.setClosable(true);
        update();
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Utilities.sleepNoException(100);
                update();
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

    public Tab getTab() {
        return tab;
    }

    public void update() {
        if(applet == null) return;

        Session session = Repository.get(applet.hashCode());
        if (session != null && session.getAccount() != null) {
            getTab().setText(session.getName());
        }

        int relX = calculateRelX();
        int relY = calculateRelY();
        if(relX < 0 || relY < 0) {
            return;
        }

        if (tab.isSelected()) {
            applet.setBounds(relX, relY, applet.getWidth(),
                    applet.getHeight());
            applet.setSize(765, 503);
            applet.setVisible(true);
        } else {
            applet.setBounds(relX - 1000, relY,
                    applet.getWidth(), applet.getHeight());
            applet.setSize(765, 503);
            applet.setVisible(false);
        }
    }

    public void onMenuOpening() {
        if (applet != null) {
            final Session session = get(applet.hashCode());
           /* CopyOnWriteArrayList<MenuItem> pluginMenu = session.getPluginMenu();
            ObservableList<MenuItem> items = FXCollections.observableArrayList();
            for (MenuItem pluginItem : pluginMenu) {
                items.add(pluginItem);
            }
            parent.getPluginsMenu().getItems().clear();
            parent.getPluginsMenu().getItems().add(parent.getPluginOverview());
            parent.getPluginsMenu().getItems().addAll(items);   */
            // TODO
        }
    }
}