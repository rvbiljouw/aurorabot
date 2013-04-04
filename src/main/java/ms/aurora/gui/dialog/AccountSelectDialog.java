package ms.aurora.gui.dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ms.aurora.core.model.Account;
import ms.aurora.gui.account.AccountModel;
import ms.aurora.gui.plugin.PluginOverview;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * User: tobiewarburton
 * Date: 04/04/2013
 * Time: 01:14
 * To change this template use File | Settings | File Templates.
 */
public class AccountSelectDialog extends AnchorPane {

    private final ObservableList<Account> accounts = FXCollections.observableArrayList();
    private Account selected;

    @FXML
    private ComboBox<Account> accountsCombo;

    @FXML
    private Button btnOk;

    public AccountSelectDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccountSelectDialog.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void ok(ActionEvent event) {
        selected = accountsCombo.getValue();
        getScene().getWindow().hide();
    }

    public Account getSelected() {
        return selected;
    }

    @FXML
    void initialize() {
        assert accountsCombo != null : "fx:id=\"accountsCombo\" was not injected: check your FXML file 'AccountSelectDialog.fxml'.";
        initializeAccounts();
    }

    private void initializeAccounts() {
        accounts.clear();
        for (Account account : Account.getAll()) {
            accounts.add(account);
        }
        accountsCombo.setItems(accounts);
    }

    public static AccountSelectDialog showDialog() {
        Stage stage = new Stage();
        stage.setTitle("Select Account");
        stage.setWidth(400);
        stage.setHeight(86);
        stage.initModality(Modality.APPLICATION_MODAL);
        AccountSelectDialog accountSelectDialog = new AccountSelectDialog();
        Scene scene = new Scene(accountSelectDialog);
        scene.getStylesheets().add("blue.css");
        stage.setScene(scene);
        stage.show();
        return accountSelectDialog;
    }
}
