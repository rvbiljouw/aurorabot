package ms.aurora.sdn.net.packet;

import ms.aurora.sdn.net.OutgoingPacket;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 24/05/13
 * Time: 22:51
 * To change this template use File | Settings | File Templates.
 */
public class FileRequest extends OutgoingPacket {
    private byte type;
    private long id;

    public FileRequest(byte type, long id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public int getOpcode() {
        return 7;
    }

    @Override
    public void prepare() throws IOException {
        getStream().writeByte(type);
        getStream().writeLong(id);
        getStream().flush();
        getStream().close();
    }
}