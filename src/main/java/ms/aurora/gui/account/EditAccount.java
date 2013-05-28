package ms.aurora.gui.account;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ms.aurora.gui.Dialog;
import ms.aurora.gui.Messages;
import ms.aurora.gui.util.FXUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ms.aurora.gui.Messages.getString;
import static ms.aurora.gui.util.FXUtils.load;

/**
 * @author Rick
 */
public class EditAccount extends Dialog {

    @FXML
    private TextField txtBankPin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    private final AccountModel accountModel;

    public EditAccount(AccountModel accountModel) {
        this.accountModel = accountModel;
        load(getClass().getResource("EditAccount.fxml"), this);
    }

    @FXML
    void onCancel(ActionEvent event) {
        close();
    }

    @FXML
    void onOk(ActionEvent event) {
        accountModel.setUsername(txtUsername.getText());
        accountModel.setPassword(txtPassword.getText());
        accountModel.setBankPin(txtBankPin.getText());
        accountModel.getAccount().update();
        close();
    }

    @FXML
    void initialize() {
        txtUsername.setText(accountModel.getUsername());
        txtPassword.setText(accountModel.getRealPassword());
        txtBankPin.setText(accountModel.getBankPin());
    }

    @Override
    public String getTitle() {
        return getString("editAccount.title");
    }
}
