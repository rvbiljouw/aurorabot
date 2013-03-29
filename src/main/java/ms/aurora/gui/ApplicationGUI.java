package ms.aurora.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ms.aurora.gui.script.ScriptOverview;
import ms.aurora.gui.widget.AppletWidget;
import ms.aurora.loader.AppletLoader;

import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ApplicationGUI extends AnchorPane {
    private static ApplicationGUI self;

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
            self = this;
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
        AppletWidget widget = new AppletWidget();
        AppletLoader loader = new AppletLoader();
        loader.setCompletionListener(widget);
        loader.start();


        tabPane.getTabs().add(widget.tab());
    }

    @FXML
    void onStartScript(ActionEvent evt) {
        if (getSelectedApplet() != null) {
            Stage stage = new Stage();
            stage.setTitle("Select a script");
            stage.setWidth(810);
            stage.setHeight(640);
            stage.initModality(Modality.APPLICATION_MODAL);
            ScriptOverview overview = new ScriptOverview();
            Scene scene = new Scene(overview);
            scene.getStylesheets().add("blue.css");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void initialize() {
        assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'Application.fxml'.";
    }

    public static Applet getSelectedApplet() {
        Tab tab = self.tabPane.getSelectionModel().getSelectedItem();
        if (tab != null && tab.getContent() instanceof AppletWidget) {
            return ((AppletWidget) tab.getContent()).getApplet();
        }
        return null;
    }
}
