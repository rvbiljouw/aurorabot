package ms.aurora.gui.account;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ms.aurora.core.model.Account;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author rvbiljouw
 */
public class NewAccount extends AnchorPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtBankPin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    private final AccountModel accountModel;

    public NewAccount() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewAccount.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            this.accountModel = new AccountModel(new Account());
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        getScene().getWindow().hide();
    }

    @FXML
    void onOk(ActionEvent event) {
        accountModel.setUsername(txtUsername.getText());
        accountModel.setPassword(txtPassword.getText());
        accountModel.setBankPin(txtBankPin.getText());
        accountModel.getAccount().save();
        getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
        assert txtBankPin != null : "fx:id=\"txtBankPin\" was not injected: check your FXML file 'NewAccount.fxml'.";
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'NewAccount.fxml'.";
        assert txtUsername != null : "fx:id=\"txtUsername\" was not injected: check your FXML file 'NewAccount.fxml'.";
        txtUsername.setText(accountModel.getUsername());
        txtPassword.setText(accountModel.getPassword());
        txtBankPin.setText(accountModel.getBankPin());
    }

}
