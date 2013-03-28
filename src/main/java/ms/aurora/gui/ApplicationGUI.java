package ms.aurora.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import ms.aurora.gui.widget.AppletWidget;
import ms.aurora.loader.AppletLoader;

import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ApplicationGUI extends AnchorPane implements AppletLoader.CompletionListener {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane tabPane;

    public ApplicationGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ApplicationGUI.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    @FXML
    void onCloseClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onNewSession(ActionEvent event) {
        AppletLoader loader = new AppletLoader();
        loader.setCompletionListener(this);
        loader.start();
    }

    @Override
    public void onCompletion(final Applet applet) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Tab tab = new Tab("[ " + applet.hashCode() + " ]");
                tab.setContent(new AppletWidget(applet));
                tabPane.getTabs().add(tab);
            }
        });
    }

    @FXML
    void initialize() {
        assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'Application.fxml'.";
    }
}
