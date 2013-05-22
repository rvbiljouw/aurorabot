package ms.aurora.gui.account;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import ms.aurora.core.model.Account;
import ms.aurora.gui.Dialog;
import ms.aurora.gui.util.FXUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author tobiewarburton
 */
public class AccountSelectDialog extends Dialog {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button btnSelect;
    @FXML
    private ComboBox<Account> cbxAccounts;

    public AccountSelectDialog() {
        FXUtils.load(getClass().getResource("AccountSelectDialog.fxml"), this);
    }

    @FXML
    void selectAction(ActionEvent event) {
        close();
    }

    @FXML
    void initialize() {
        assert btnSelect != null : "fx:id=\"btnSelect\" was not injected: check your FXML file 'AccountSelectDialog.fxml'.";
        assert cbxAccounts != null : "fx:id=\"cbxAccounts\" was not injected: check your FXML file 'AccountSelectDialog.fxml'.";
        cbxAccounts.setItems(FXCollections.observableArrayList(Account.getAll()));
    }

    public Account get() {
        return cbxAccounts.getSelectionModel().getSelectedItem();
    }

    @Override
    public String getTitle() {
        return "Select account";
    }
}

