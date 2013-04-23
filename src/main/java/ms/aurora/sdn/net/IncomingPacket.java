package ms.aurora.sdn.net;

import java.io.DataInputStream;

/**
 * @author rvbiljouw
 */
public class IncomingPacket {
    private final DataInputStream dis;
    private final int opcode;

    public IncomingPacket(int opcode, DataInputStream dis) {
        this.opcode = opcode;
        this.dis = dis;
    }

    /**
     * Retrieves the opcode of the packet
     *
     * @return
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * Gets the stream for the package
     *
     * @return stream
     */
    public DataInputStream getStream() {
        return dis;
    }

}
