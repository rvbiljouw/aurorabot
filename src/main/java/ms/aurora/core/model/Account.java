package ms.aurora.core.model;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;


@Entity
@NamedQueries({
        @NamedQuery(name = "account.getAll", query = "select a from Account a")
})
public class Account extends AbstractModel {
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
        return decrypt(password);
    }


    public void setPassword(String password) {
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
        }
        return new String(output);
    }

    private static String getKey() {
        String hwid = null;
        try {
            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            hwid = new String(Base64.encodeBase64(ni.getHardwareAddress()));
        } catch (Exception e) {
            hwid = new String(Base64.encodeBase64(System.getProperty("user.home").getBytes()));
        }
        return hwid;
    }
}
