package ms.aurora.core.model;

import ms.aurora.gui.dialog.MasterPasswordDialog;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import javax.swing.*;
import java.util.List;


@Entity
@NamedQueries({
        @NamedQuery(name = "account.getAll", query = "select a from Account a")
})
public class Account extends AbstractModel {
    private static String masterPassword;

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

    public static List<Account> getAll() {
        return getEm().createNamedQuery("account.getAll", Account.class).getResultList();
    }

    private static String encrypt(String input) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(getKey().getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return new String(Base64.encodeBase64(crypted));
    }

    private static String decrypt(String input) {
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(getKey().getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decodeBase64(input.getBytes()));
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return new String(output);
    }

    private static String getKey() {
        if(masterPassword == null) {
            masterPassword = MasterPasswordDialog.showDialog().get();
        }
        return masterPassword;
    }
}
