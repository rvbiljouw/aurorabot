package ms.aurora.sdn.net.encode;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

/**
 * @author rvbiljouw
 */
public class MD5 {

    public static String digest(File file) throws Exception {
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


}
