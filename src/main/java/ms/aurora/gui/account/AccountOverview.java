package ms.aurora.gui.account;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import jfx.messagebox.MessageBox;
import ms.aurora.core.model.Account;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author rvbiljouw
 */
public class AccountOverview extends AnchorPane {

    private final ObservableList<AccountModel> accounts = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<AccountModel, String> colBankPin;

    @FXML
    private TableColumn<AccountModel, String> colPassword;

    @FXML
    private TableColumn<AccountModel, String> colUsername;

    @FXML
    private TableView<AccountModel> tblAccounts;

    public AccountOverview() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccountOverview.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
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
    void onNewAccount(ActionEvent event) {
        AccountModel newModel = new AccountModel(new Account());
        accounts.add(newModel);
    }

    @FXML
    void onOk(ActionEvent event) {
        for (AccountModel model : accounts) {
            Account account = model.getAccount();
            if (account.getUsername() == null || account.getUsername().length() == 0) {
                MessageBox.show(getScene().getWindow(), "Username is required.", "Error!", MessageBox.OK);
                return;
            } else if (account.getPassword() == null || account.getPassword().length() == 0) {
                MessageBox.show(getScene().getWindow(), "Password is required.", "Error!", MessageBox.OK);
                return;
            } else {
                if (account.getId() == null) {
                    account.save();
                } else {
                    account.update();
                }
            }
        }
        getScene().getWindow().hide();
    }

    @FXML
    void onRemoveSelected(ActionEvent event) {
        ObservableList<AccountModel> selectedAccounts = tblAccounts.getSelectionModel().getSelectedItems();
        for (AccountModel model : selectedAccounts) {
            accounts.remove(model);
            model.getAccount().remove();
        }
        tblAccounts.setItems(accounts);
    }

    @FXML
    void initialize() {
        assert colBankPin != null : "fx:id=\"colBankPin\" was not injected: check your FXML file 'AccountOverview.fxml'.";
        assert colPassword != null : "fx:id=\"colPassword\" was not injected: check your FXML file 'AccountOverview.fxml'.";
        assert colUsername != null : "fx:id=\"colUsername\" was not injected: check your FXML file 'AccountOverview.fxml'.";
        assert tblAccounts != null : "fx:id=\"tblAccounts\" was not injected: check your FXML file 'AccountOverview.fxml'.";

        colUsername.setCellValueFactory(new PropertyValueFactory<AccountModel, String>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<AccountModel, String>("password"));
        colBankPin.setCellValueFactory(new PropertyValueFactory<AccountModel, String>("bankPin"));

        for (Account account : Account.getAll()) {
            AccountModel model = new AccountModel(account);
            accounts.add(model);
        }
        tblAccounts.setItems(accounts);
        tblAccounts.setEditable(true);
        tblAccounts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public static class AccountModel {
        private final Account account;
        private StringProperty username;
        private StringProperty password;
        private StringProperty bankPin;


        public AccountModel(Account account) {
            this.account = account;
            this.username = new SimpleStringProperty(account.getUsername());
            this.password = new SimpleStringProperty(account.getPassword());
            this.bankPin = new SimpleStringProperty(account.getBankPin());
        }

        public String getUsername() {
            return username.get();
        }

        public void setUsername(String username) {
            getAccount().setUsername(username);
            this.username.set(username);
        }

        public String getPassword() {
            return password.get();
        }

        public void setPassword(String password) {
            getAccount().setPassword(password);
            this.password.set(password);
        }

        public String getBankPin() {
            return bankPin.get();
        }

        public void setBankPin(String bankPin) {
            getAccount().setBankPin(bankPin);
            this.bankPin.set(bankPin);
        }

        public Account getAccount() {
            return account;
        }
    }
}
