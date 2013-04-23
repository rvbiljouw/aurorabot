package ms.aurora.sdn.net.packet;

import ms.aurora.sdn.net.OutgoingPacket;
import ms.aurora.sdn.net.api.Versioning;
import ms.aurora.sdn.net.encode.MD5;

import java.io.File;
import java.io.IOException;

/**
 * @author rvbiljouw
 */
public class UpdateRequest extends OutgoingPacket {
    private final File file;

    public UpdateRequest(File file) {
        this.file = file;
    }

    @Override
    public int getOpcode() {
        return 2;
    }

    @Override
    public void prepare() throws IOException {
        try {
            String digest = MD5.digest(file);
            getStream().writeInt(2);
            getStream().writeUTF(digest);
            getStream().flush();
        } catch (Exception e) {
            throw new IOException("Hashing exception..");
        }
    }
}
