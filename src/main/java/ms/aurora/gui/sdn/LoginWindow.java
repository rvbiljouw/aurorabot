package ms.aurora.gui.sdn;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfx.messagebox.MessageBox;
import ms.aurora.gui.Messages;
import ms.aurora.core.model.Property;
import ms.aurora.sdn.net.api.Authentication;
import ms.aurora.sdn.net.encode.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
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

    @FXML
    private CheckBox cbxRemember;

    public LoginWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginWindow.fxml"), Messages.getBundle());

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

    @FXML
    void onPasswordKeyRelease(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            onAuthenticate(null);
        }
    }

    public void setMessage(final String msg) {
        final LoginWindow window = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(currentStage == null) {
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
                            cbxRemember.setSelected(true);
                        }
                    });
                }

                MessageBox.show(currentStage, msg, Messages.getString("MesssageBox.MESSAGE"), MessageBox.OK);
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
        Property user = Property.getByName("forumuser");
        if (user != null) {
            Property pwd = Property.getByName(user.getValue());
            Authentication.login(decrypt(pwd.getName()), decrypt(pwd.getValue()));
        } else {
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
    }

    public void close() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (currentStage != null) {
                    if (cbxRemember.isSelected()) {
                        Property user = new Property();
                        Property password = new Property();

                        password.setName(encrypt(txtUsername.getText()));
                        password.setValue(encrypt(pwdPassword.getText()));
                        password.save();

                        user.setName("forumuser");
                        user.setValue(encrypt(txtUsername.getText()));
                        user.save();
                    }
                    currentStage.close();
                }
            }
        });
    }

    /*
 * CRYPTO FUNCTIONS
 */
    private static String encrypt(String input) {
        byte[] crypted = null;
        try {
            SecretKeySpec key = new SecretKeySpec(pad(getKey()), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return new String(Base64.encode(crypted));
    }

    private static String decrypt(String input) {
        byte[] output = null;
        try {
            SecretKeySpec key = new SecretKeySpec(pad(getKey()), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            // output = cipher.doFinal(Base64.decodeBase64(input.getBytes()));
            output = cipher.doFinal(Base64.decode(input));

        } catch (Exception ignored) {
            // password is wrong...
        }
        if (output != null) {
            return new String(output);
        } else {
            return "";
        }
    }

    private static byte[] pad(String value) {
        if (value == null) return null;

        if (value.length() > 16) {
            return value.substring(0, 16).getBytes();
        } else {
            StringBuffer buff = new StringBuffer();
            while (buff.length() + value.length() < 16) {
                buff.append("0");
            }
            buff.append(value);
            return buff.toString().getBytes();
        }
    }

    private static String getKey() {
        return Property.getByName("masterPassword").getValue();
    }
}
