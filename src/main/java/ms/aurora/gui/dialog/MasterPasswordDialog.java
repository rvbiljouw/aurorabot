package ms.aurora.gui.dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ms.aurora.core.model.Account;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 04/04/2013
 * Time: 02:56
 * To change this template use File | Settings | File Templates.
 */
public class MasterPasswordDialog extends AnchorPane {
    @FXML
    private Button btnAuthenticate;
    @FXML
    private PasswordField txtPassword;

    public MasterPasswordDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MasterPasswordDialog.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void authenticate(ActionEvent event) {
        getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
    }

    public String get() {
        return txtPassword.getText();
    }

    public static MasterPasswordDialog showDialog() {
        Stage stage = new Stage();
        stage.setWidth(286);
        stage.setHeight(86);
        stage.initModality(Modality.APPLICATION_MODAL);
        MasterPasswordDialog dialog = new MasterPasswordDialog();
        Scene scene = new Scene(dialog);
        scene.getStylesheets().add("blue.css");
        stage.setScene(scene);
        stage.show();
        return dialog;
    }
}
