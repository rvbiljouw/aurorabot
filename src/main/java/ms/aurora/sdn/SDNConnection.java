package ms.aurora.sdn;

import ms.aurora.Application;
import ms.aurora.gui.swing.Login;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

/**
 * @author rvbiljouw
 */
public class SDNConnection {
    private static Logger logger = Logger.getLogger(SDNConnection.class);
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket socket;
    private final Login frame;

    public SDNConnection(Login frame) {
        this.frame = frame;
    }

    public void connect() {
        try {
            socket = new Socket("208.94.241.76", Integer.parseInt("65500"));
            socket.setKeepAlive(true);
            // Use Integer.parseInt so the 65549
            // gets taken in with the string obbing.
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            initCrypto();
            dos.writeInt(0);
            dos.writeUTF(DatatypeConverter.printBase64Binary(encrypt(frame.getUsername().getBytes())));
            dos.writeUTF(DatatypeConverter.printBase64Binary(encrypt(frame.getPassword().getBytes())));
            dos.flush();
            String rawRetMsg = new String(decrypt(DatatypeConverter.parseBase64Binary(dis.readUTF())));
            if (rawRetMsg.equals("ok")) {
                checkUpdate();
                Application.init();
                frame.setVisible(false);
            } else {
                frame.setMessage(rawRetMsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(255);
        }
    }

    private void checkUpdate() {
        try {
            String path = Application.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            if (!path.endsWith(".jar")) {
                logger.info("Not running inside jar, not checking for updates!");
                return;
            }
            File file = new File(path);
            String digest = digest(file);
            dos.writeInt(1);
            dos.writeUTF(digest);
            dos.flush();

            int response = dis.readInt();
            switch (response) {
                case 0:
                    logger.info("Client is up-to-date");
                    break;

                case 1:
                    logger.info("Update available.");
                    FileOutputStream fos = new FileOutputStream(file);
                    long fileSize = dis.readLong();
                    long totalRead = 0;
                    int read = 0;
                    byte[] buffer = new byte[256];
                    while(totalRead != fileSize) {
                        read = dis.read(buffer);
                        totalRead += read;
                        fos.write(buffer, 0, read);
                    }
                    fos.flush();
                    fos.close();
                    JOptionPane.showMessageDialog(null, "The client was updated, please re-start!");
                    System.exit(0);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initCrypto() {
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

    private byte[] decrypt(byte[] data) {
        try {
            return decCipher.doFinal(data);
        } catch (Exception e) {
            System.exit(252);
        }
        return new byte[0];
    }

    private byte[] encrypt(byte[] data) {
        try {
            return encCipher.doFinal(data);
        } catch (Exception e) {
            System.exit(251);
        }
        return new byte[0];
    }

    private static byte[] pad(byte[] array) {
        List<Byte> bytes = new ArrayList<Byte>();
        int i = 0;
        while (i == 0 || i % 16 != 0) {
            if (i < array.length) {
                bytes.add(array[i]);
            } else {
                bytes.add((byte) 0);
            }
            i++;
        }
        byte[] raw = new byte[bytes.size()];
        for (int j = 0; j < raw.length; j++) {
            raw[j] = bytes.get(j);
        }
        return raw;
    }

    private static String digest(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream(file);
        try {
            is = new DigestInputStream(is, md);
            while(is.read(new byte[256]) != -1);
        }
        finally {
            is.close();
        }
        byte[] digest = md.digest();
        return printBase64Binary(digest);
    }

    private Cipher encCipher;
    private Cipher decCipher;
}
