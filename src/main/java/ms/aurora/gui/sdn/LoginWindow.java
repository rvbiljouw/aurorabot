package ms.aurora.gui.sdn;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfx.messagebox.MessageBox;
import ms.aurora.core.model.Property;
import ms.aurora.api.javafx.Dialog;
import ms.aurora.gui.Messages;
import ms.aurora.sdn.net.api.Authentication;
import ms.aurora.sdn.net.encode.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static ms.aurora.api.javafx.FXUtils.load;

/**
 * @author tobiewarburton
 */
public class LoginWindow extends Dialog {

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
        load(getClass().getResource("LoginWindow.fxml"), this);
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
        return Base64.encode(crypted);
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

    @FXML
    void onAuthenticate(ActionEvent event) {
        String username = txtUsername.getText();
        String password = pwdPassword.getText();
        btnAuthenticate.setDisable(true);
        Authentication.login(username, password);
    }

    @FXML
    void onPasswordKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onAuthenticate(null);
        }
    }

    public void setMessage(final String msg) {
        final LoginWindow window = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (currentStage == null) {
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

    }

    @Override
    public void showAndWait() {
        Property user = Property.getByName("forumuser");
        if (user != null) {
            Property pwd = Property.getByName(user.getValue());
            Authentication.login(decrypt(pwd.getName()), decrypt(pwd.getValue()));
        } else {
            super.showAndWait();
        }
    }

    @Override
    public void onClose() {
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
        }
    }

    @Override
    public String getTitle() {
        return "Authenticate";
    }
}
