package ms.aurora.gui.account;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import ms.aurora.core.model.Account;
import ms.aurora.gui.Dialog;
import ms.aurora.gui.Messages;
import ms.aurora.gui.util.FXUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Rick
 */
public class AccountOverview extends Dialog {

    private final ObservableList<AccountModel> accounts = FXCollections.observableArrayList();
    private final EventHandler<WindowEvent> closeHandler = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent windowEvent) {
            initializeTable();
        }
    };
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccountOverview.fxml"), Messages.getBundle());

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
        FXUtils.showModalDialog(Messages.getString("newAccount.title"), new NewAccount(), closeHandler);
    }

    @FXML
    void onOk(ActionEvent event) {
        getScene().getWindow().hide();
    }

    @FXML
    void onRemoveSelected(ActionEvent event) {
        AccountModel selectedAccount = tblAccounts.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            selectedAccount.getAccount().remove();
            accounts.remove(selectedAccount);
        }
        initializeTable();
    }

    @FXML
    void onEditSelected(ActionEvent event) {
        AccountModel selectedAccount = tblAccounts.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            FXUtils.showModalDialog(Messages.getString("editAccount.title"),
                    new EditAccount(selectedAccount), closeHandler);
        }
    }

    @FXML
    void initialize() {
        colUsername.setCellValueFactory(new PropertyValueFactory<AccountModel, String>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<AccountModel, String>("password"));
        colBankPin.setCellValueFactory(new PropertyValueFactory<AccountModel, String>("bankPin"));

        initializeTable();
    }

    private void initializeTable() {
        accounts.clear();
        for (Account account : Account.getAll()) {
            AccountModel model = new AccountModel(account);
            accounts.add(model);
        }
        tblAccounts.setItems(accounts);
    }

    @Override
    public String getTitle() {
        return Messages.getString("accountOverview.title");
    }
}
