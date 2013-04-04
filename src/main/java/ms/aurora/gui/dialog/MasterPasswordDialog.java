package ms.aurora.gui.dialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * @author tobiewarburton
 */
public class MasterPasswordDialog extends AnchorPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAuthenticate;

    @FXML
    private PasswordField txtPassword;

    private Callback callback;

    public MasterPasswordDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MasterPasswordDialog.fxml"));

        //fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            this.getChildren().add((Parent) fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    @FXML
    void authenticateAction(ActionEvent event) {
        getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
        assert btnAuthenticate != null : "fx:id=\"btnAuthenticate\" was not injected: check your FXML file 'MasterPasswordDialog.fxml'.";
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'MasterPasswordDialog.fxml'.";
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }


    public String get() {
        return txtPassword.getText();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Authenticate");
        stage.setWidth(250);
        stage.setHeight(100);
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(this);
        scene.getStylesheets().add("blue.css");
        stage.setScene(scene);
        stage.setScene(scene);
        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                getCallback().call();
            }
        });
        stage.show();
    }
}
