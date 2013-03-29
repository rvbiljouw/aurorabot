package ms.aurora.gui.widget;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import ms.aurora.Application;
import ms.aurora.loader.AppletLoader;

import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author rvbiljouw
 */
public class AppletWidget extends AnchorPane implements AppletLoader.CompletionListener {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane appletPane;

    private Applet applet;

    public AppletWidget() {
        this.loadFace();
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

    @Override
    public void onCompletion(final Applet applet) {
        visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                toggleVisibility(observableValue, applet);
            }
        });

        toggleVisibility(visibleProperty(), applet);
        Application.registerApplet(applet);
        this.applet = applet;
    }

    private void toggleVisibility(ObservableValue<? extends Boolean> observableValue, Applet applet) {
        if (observableValue.getValue()) {
            applet.setBounds(calculateRelX(), calculateRelY(), applet.getWidth(),
                    applet.getHeight());
            applet.setSize(765, 503);
            applet.setVisible(true);
        } else {
            applet.setBounds(calculateRelX() - applet.getWidth(), calculateRelY(),
                    applet.getWidth(), applet.getHeight());
            applet.setSize(765, 503);
            applet.setVisible(false);
        }
    }

    public Applet getApplet() {
        return applet;
    }
}
