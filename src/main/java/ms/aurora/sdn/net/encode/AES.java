package ms.aurora.sdn.net.encode;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author rvbiljouw
 */
public class AES {
    private static Cipher encCipher;
    private static Cipher decCipher;

    public static byte[] decrypt(byte[] data) {
        try {
            return decCipher.doFinal(data);
        } catch (Exception e) {
            System.exit(252);
        }
        return new byte[0];
    }

    public static String decryptString(byte[] data) {
        return new String(decrypt(data));
    }

    public static byte[] encrypt(byte[] data) {
        try {
            return encCipher.doFinal(data);
        } catch (Exception e) {
            System.exit(251);
        }
        return new byte[0];
    }

    public static byte[] encryptString(String data) {
        return encrypt(data.getBytes());
    }

    static {
        byte[] key = "8712364587123645".getBytes();
        SecretKeySpec secret = new SecretKeySpec(key, "AES");
        try {
            decCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            decCipher.init(Cipher.DECRYPT_MODE, secret);
            encCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            encCipher.init(Cipher.ENCRYPT_MODE, secret);
        } catch (Exception e) {
            System.exit(253);
        }
    }
}
