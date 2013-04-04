package ms.aurora.gui.dialog;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ms.aurora.core.model.Account;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

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

    private Callback callback;

    public AccountSelectDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccountSelectDialog.fxml"));

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
        getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
        assert btnSelect != null : "fx:id=\"btnSelect\" was not injected: check your FXML file 'AccountSelectDialog.fxml'.";
        assert cbxAccounts != null : "fx:id=\"cbxAccounts\" was not injected: check your FXML file 'AccountSelectDialog.fxml'.";
        cbxAccounts.setItems(FXCollections.observableArrayList(Account.getAll()));
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public Account get() {
        return cbxAccounts.getSelectionModel().getSelectedItem();
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Select Account");
        stage.setWidth(250);
        stage.setHeight(100);
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(this);
        scene.getStylesheets().add("blue.css");
        stage.setScene(scene);
        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                getCallback().call();
            }
        });
        stage.show();
    }
}

