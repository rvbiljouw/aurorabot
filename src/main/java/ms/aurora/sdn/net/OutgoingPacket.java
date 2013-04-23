package ms.aurora.sdn.net;

import java.io.*;

/**
 * @author rvbiljouw
 */
public abstract class OutgoingPacket {
    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private DataOutputStream dos;

    public OutgoingPacket() {
        try {
            dos = new DataOutputStream(os);
            dos.writeInt(getOpcode());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize outgoing package.");
        }
    }

    /**
     * Retrieves the opcode of the packet
     *
     * @return
     */
    public abstract int getOpcode();

    /**
     * Prepares the packet for being sent
     */
    public abstract void prepare() throws IOException;

    /**
     * Gets the stream for the package
     *
     * @return stream
     */
    public DataOutputStream getStream() {
        return dos;
    }

    /**
     * Gets the payload for this packet.
     * @return payload
     */
    public byte[] getPayload() {
        return os.toByteArray();
    }
}
