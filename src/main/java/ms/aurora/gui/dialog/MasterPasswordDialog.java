package ms.aurora.gui.dialog;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import ms.aurora.api.javafx.Dialog;
import ms.aurora.api.javafx.FXUtils;
import ms.aurora.core.model.Property;
import ms.aurora.gui.Messages;

import static ms.aurora.core.model.Property.getByName;

/**
 * @author tobiewarburton
 */
@SuppressWarnings("unused")
public class MasterPasswordDialog extends Dialog {

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
    private StringProperty value;
    private Property property;

    public MasterPasswordDialog(StringProperty value) {
        this.property = getByName("masterPassword");
        this.value = value;


        FXUtils.load(getClass().getResource("MasterPasswordDialog.fxml"), this);
    }

    @FXML
    void onAuthenticate() {
        if (authenticated()) {
            String password = txtPassword.getText();
            if (property == null) {
                Property masterPass = new Property();
                masterPass.setName("masterPassword");
                masterPass.setValue(password);
                masterPass.save();
                close();
            } else if (password.equalsIgnoreCase(property.getValue())) {
                close();
            }

            value.setValue(password);
        }

        if (txtPassword.getText().length() > 0 && txtVerifyPassword.getLength() > 0) {
            showWarning();
        }
    }

    @FXML
    void onPasswordKeyReleased(KeyEvent event) {
        if (property != null && event.getCode() == KeyCode.ENTER) {
            onAuthenticate();
        }
    }

    @FXML
    void onPasswordRepeatKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onAuthenticate();
        }
    }

    @FXML
    void onCancel() {
        System.exit(0);
    }

    @Override
    public void onClose() {
        if (!authenticated()) {
            System.exit(0);
        }
    }

    @FXML
    void initialize() {
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

    public boolean authenticated() {
        return txtPassword.getText().length() != 0 && (property != null ||
                (txtVerifyPassword.getText().length() != 0
                        && txtPassword.getText().equalsIgnoreCase(txtVerifyPassword.getText())));
    }

    @Override
    public String getTitle() {
        return "Authenticate";
    }
}
