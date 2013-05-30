package ms.aurora.gui.dialog;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ms.aurora.core.model.Account;
import ms.aurora.gui.Messages;
import ms.aurora.api.javafx.FXUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author tobiewarburton
 */
public class AccountSelectDialog extends AnchorPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnSelect;

    @FXML
    private ComboBox<Account> cbxAccounts;

    private Stage currentStage;

    public AccountSelectDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccountSelectDialog.fxml"), Messages.getBundle());

        //fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            this.getChildren().add((Parent) fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void selectAction(ActionEvent event) {
        currentStage.close();
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

    public void showAndWait() {
        currentStage = FXUtils.createModalStage(Messages.getString("accountSelect.title"), this);
        currentStage.showAndWait();
    }
}
