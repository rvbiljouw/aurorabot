package ms.aurora.sdn.net;

import java.io.IOException;

/**
 * @author rvbiljouw
 */
public interface PacketHandler {

    /**
     * Gets the accepted opcode for this handler
     * @return opcode
     */
    public int getOpcode();

    /**
     * Handles the incomingPacket
     * @param incomingPacket
     */
    public void handle(IncomingPacket incomingPacket) throws IOException;

}
