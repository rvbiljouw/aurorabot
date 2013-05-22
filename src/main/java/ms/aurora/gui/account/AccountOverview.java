package ms.aurora.gui.account;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import ms.aurora.core.model.Account;
import ms.aurora.gui.Dialog;
import ms.aurora.gui.util.FXUtils;

import static ms.aurora.gui.Messages.getString;
import static ms.aurora.gui.util.FXUtils.showModalDialog;

/**
 * @author Rick
 */
public class AccountOverview extends Dialog {
    private final ObservableList<AccountModel> accounts = FXCollections.observableArrayList();
    private final EventHandler<WindowEvent> closeHandler = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent windowEvent) {
            rebuild();
        }
    };

    @FXML
    private TableColumn<AccountModel, String> colBankPin;
    @FXML
    private TableColumn<AccountModel, String> colPassword;
    @FXML
    private TableColumn<AccountModel, String> colUsername;
    @FXML
    private TableView<AccountModel> tblAccounts;

    public AccountOverview() {
        FXUtils.load(getClass().getResource("AccountOverview.fxml"), this);
    }

    @FXML
    void onCancel(ActionEvent event) {
        close();
    }

    @FXML
    void onOk() {
        close();
    }

    @FXML
    void onNewAccount() {
        showModalDialog(getString("newAccount.title"), new NewAccount(), closeHandler);
    }

    @FXML
    void onRemoveSelected() {
        AccountModel selectedAccount = tblAccounts.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            selectedAccount.getAccount().remove();
            accounts.remove(selectedAccount);
        }
        rebuild();
    }

    @FXML
    void onEditSelected() {
        AccountModel selectedAccount = tblAccounts.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            showModalDialog(getString("editAccount.title"),
                    new EditAccount(selectedAccount), closeHandler);
        }
    }

    @FXML
    void initialize() {
        colUsername.setCellValueFactory(new PropertyValueFactory<AccountModel, String>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<AccountModel, String>("password"));
        colBankPin.setCellValueFactory(new PropertyValueFactory<AccountModel, String>("bankPin"));

        rebuild();
    }

    /**
     * Rebuilds the account table
     */
    private void rebuild() {
        accounts.clear();
        for (Account account : Account.getAll()) {
            AccountModel model = new AccountModel(account);
            accounts.add(model);
        }
        tblAccounts.setItems(accounts);
    }

    @Override
    public String getTitle() {
        return getString("accountOverview.title");
    }
}
