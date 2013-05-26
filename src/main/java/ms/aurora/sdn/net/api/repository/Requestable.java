package ms.aurora.sdn.net.api.repository;

import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.api.Repository;
import ms.aurora.sdn.net.packet.FileRequest;

/**
 * @author tobiewarburton
 */
public class Requestable {
    public static final Object request_lock = new Object();
    public static final byte SCRIPT_TYPE = 1;
    public static final byte PLUGIN_TYPE = 0;

    private byte type;
    private long id;
    private byte[] content;

    public Requestable(byte type, long id) {
        this.type = type;
        this.id = id;
    }

    public byte getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public byte[] get() {
        if (content == null) {
            synchronized (request_lock) {
                SDNConnection.getInstance().writePacket(new FileRequest(type, id));
                try {
                    request_lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            content = Repository.store;
            Repository.store = null;
        }
        return content;
    }
}
