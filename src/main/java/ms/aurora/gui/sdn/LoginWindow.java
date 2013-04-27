package ms.aurora.gui.sdn;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfx.messagebox.MessageBox;
import ms.aurora.sdn.net.api.Authentication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author tobiewarburton
 */
public class LoginWindow extends AnchorPane {
    private Stage currentStage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAuthenticate;

    @FXML
    private Label lblInfo;

    @FXML
    private PasswordField pwdPassword;

    @FXML
    private TextField txtUsername;

    public LoginWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void onAuthenticate(ActionEvent event) {
        String username = txtUsername.getText();
        String password = pwdPassword.getText();
        btnAuthenticate.setDisable(true);
        Authentication.login(username, password);
    }

    public void setMessage(final String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MessageBox.show(currentStage, msg, "Message", MessageBox.OK);
                btnAuthenticate.setDisable(false);
            }
        });
    }

    @FXML
    void initialize() {
        assert btnAuthenticate != null : "fx:id=\"btnAuthenticate\" was not injected: check your FXML file 'LoginWindow.fxml'.";
        assert lblInfo != null : "fx:id=\"lblInfo\" was not injected: check your FXML file 'LoginWindow.fxml'.";
        assert pwdPassword != null : "fx:id=\"pwdPassword\" was not injected: check your FXML file 'LoginWindow.fxml'.";
        assert txtUsername != null : "fx:id=\"txtUsername\" was not injected: check your FXML file 'LoginWindow.fxml'.";
    }

    public void display() {
        final LoginWindow window = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                currentStage = new Stage();
                currentStage.setScene(new Scene(window));
                currentStage.centerOnScreen();
                currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        Platform.exit();
                        System.exit(0);
                    }
                });
                currentStage.showAndWait();
                currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
        });
    }

    public void close() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                currentStage.close();
            }
        });
    }
}
