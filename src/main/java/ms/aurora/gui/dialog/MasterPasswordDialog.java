package ms.aurora.gui.dialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ms.aurora.Application;
import ms.aurora.core.model.Property;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author tobiewarburton
 */
public class MasterPasswordDialog extends AnchorPane {

    private Stage stage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAuthenticate;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtVerifyPassword;

    @FXML
    private Label caption;

    @FXML
    private Label warning;

    private Callback callback;
    private List<Property> properties;

    public MasterPasswordDialog() {
        properties = Property.getByName("masterPassword");
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
        if (authenticated()) {
            if (properties.size() == 0) {
                Property masterPass = new Property();
                masterPass.setName("masterPassword");
                masterPass.setValue(get());
                masterPass.save();
                getScene().getWindow().hide();
            } else if (get().equalsIgnoreCase(properties.get(0).getValue())) {
                stage.close();
            }
        }
        showWarning();
    }

    @FXML
    void onCancel(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void initialize() {
        assert btnAuthenticate != null : "fx:id=\"btnAuthenticate\" was not injected: check your FXML file 'MasterPasswordDialog.fxml'.";
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'MasterPasswordDialog.fxml'.";
        assert txtVerifyPassword != null : "fx:id=\"txtVerifyPassword\" was not injected: check your FXML file 'MasterPasswordDialog.fxml'.";
        assert warning != null : "fx:id=\"warning\" was not injected: check your FXML file 'MasterPasswordDialog.fxml'.";
        assert caption != null : "fx:id=\"caption\" was not injected: check your FXML file 'MasterPasswordDialog.fxml'.";
        caption.setText(properties.size() == 0 ? "Create password" : "Unlock database");
    }

    private void showWarning() {
        warning.setTextFill(Color.web("#CC0000"));
        warning.setVisible(true);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public boolean authenticated() {
        return txtPassword.getText().length() != 0
                && txtVerifyPassword.getText().length() != 0
                && txtPassword.getText().equalsIgnoreCase(txtVerifyPassword.getText());
    }

    public String get() {
        return txtPassword.getText();
    }

    public void show() {
        stage = new Stage();
        stage.setTitle(properties.size() == 0 ? "Create Password" : "Unlock database");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(Application.mainStage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        Scene scene = new Scene(this, 433, 291);
        scene.getStylesheets().add("soft-responsive.css");
        stage.setScene(scene);
        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                getCallback().call();
            }
        });
        stage.centerOnScreen();
        stage.show();
        stage.requestFocus();
    }
}
