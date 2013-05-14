package ms.aurora.gui.dialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ms.aurora.Messages;
import ms.aurora.core.model.Property;
import ms.aurora.gui.util.FXUtils;

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
    private Label lblVerifyPassword;

    @FXML
    private Label caption;

    @FXML
    private Label warning;

    private Callback callback;
    private Property property;

    public MasterPasswordDialog() {
        property = Property.getByName("masterPassword");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MasterPasswordDialog.fxml"), Messages.getBundle());

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
            if (property == null) {
                Property masterPass = new Property();
                masterPass.setName("masterPassword");
                masterPass.setValue(get());
                masterPass.save();
                stage.close();
            } else if (get().equalsIgnoreCase(property.getValue())) {
                stage.close();
            }

            if (getCallback() != null) {
                getCallback().call();
            }
        }

        if (txtPassword.getText().length() > 0 && txtVerifyPassword.getLength() > 0) {
            showWarning();
        }
    }

    @FXML
    void onPasswordKeyReleased(KeyEvent event) {
        if (property != null && event.getCode() == KeyCode.ENTER) {
            authenticateAction(null);
        }
    }

    @FXML
    void onPasswordRepeatKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            authenticateAction(null);
        }
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
        caption.setText(property == null ? Messages.getString("masterPassword.create") :
                Messages.getString("masterPassword.unlock"));

        if (property != null) {
            lblVerifyPassword.setVisible(false);
            txtVerifyPassword.setVisible(false);
        }
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
        return txtPassword.getText().length() != 0 && (property != null ||
                (txtVerifyPassword.getText().length() != 0
                        && txtPassword.getText().equalsIgnoreCase(txtVerifyPassword.getText())));
    }

    public String get() {
        return txtPassword.getText();
    }

    public void show() {
        String title = property == null ? Messages.getString("masterPassword.create") :
                Messages.getString("masterPassword.unlock");
        stage = FXUtils.createModalStage(title, this);
        stage.centerOnScreen();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        stage.showAndWait();
    }
}
