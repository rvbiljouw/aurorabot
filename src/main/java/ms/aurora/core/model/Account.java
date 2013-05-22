package ms.aurora.core.model;

import com.avaje.ebean.Ebean;
import javafx.beans.property.StringProperty;
import ms.aurora.gui.Dialog;
import ms.aurora.gui.dialog.Callback;
import ms.aurora.gui.dialog.MasterPasswordDialog;
import ms.aurora.sdn.net.encode.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import java.util.List;


/**
 * Represents an Account in the database.
 * WARNING: it's important that this class is properly secured
 * by the SecurityManager, to prevent accounts from being hijacked.
 */
@Entity
public class Account extends AbstractModel {
    private static StringProperty masterPassword;

    @Id
    @GeneratedValue
    private Long id;
    private String username = "";
    private String password = "";
    private String bankPin = "0000";

    public Account() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        if (password == null) return null;
        return decrypt(password);
    }


    public void setPassword(String password) {
        if (password == null) this.password = null;
        this.password = encrypt(password);
    }


    public String getBankPin() {
        return bankPin;
    }


    public void setBankPin(String bankPin) {
        this.bankPin = bankPin;
    }

    @Override
    public String toString() {
        return username;
    }

    public static List<Account> getAll() {
        return Ebean.find(Account.class).findList();
    }

    /*
     * CRYPTO FUNCTIONS
     */
    public static String encrypt(String input) {
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
        return masterPassword.getValue();
    }

    public static void init() {
        Dialog dialog = new MasterPasswordDialog(masterPassword);
        dialog.show();
    }
}
