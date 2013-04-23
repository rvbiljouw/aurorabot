package ms.aurora.sdn.net.encode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rvbiljouw
 */
public class Generic {

    /**
     * Pads a byte array to multiple of 16-byte length
     * @param array
     * @return padded array
     */
    public static byte[] pad(byte[] array) {
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

}
