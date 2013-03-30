package ms.aurora.gui.widget;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import ms.aurora.Application;
import ms.aurora.api.util.Utilities;
import ms.aurora.core.Session;
import ms.aurora.core.SessionRepository;
import ms.aurora.gui.ApplicationGUI;
import ms.aurora.loader.AppletLoader;

import javax.swing.*;
import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author rvbiljouw
 */
public class AppletWidget extends AnchorPane implements AppletLoader.CompletionListener, ChangeListener<Boolean> {
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
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Utilities.sleepNoException(100);

                Session session = SessionRepository.get(applet.hashCode());
                toggleVisibility(tab().isSelected(), applet);
                if (session != null) {
                    if (tab().isSelected()) {
                        session.injectPluginMenu(parent.getPluginsMenu());
                    } else {
                        session.pullOutPluginMenu(parent.getPluginsMenu());
                    }
                }
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
            System.out.println("Applet now invisible. " + relX + "," + relY);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tab().setText("[ Session " + applet.hashCode() + " ]");
                }
            });
        }
    }
}
