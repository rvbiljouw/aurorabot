package ms.aurora.sdn.net.packet;

import flexjson.JSONSerializer;
import ms.aurora.sdn.model.RegionDataPacket;
import ms.aurora.sdn.net.OutgoingPacket;

import java.io.IOException;

/**
 * @author tobiewarburton
 */
public class RegionData extends OutgoingPacket {
    private RegionDataPacket check;

    public RegionData(RegionDataPacket check) {
        this.check = check;
    }

    @Override
    public int getOpcode() {
        return 9;
    }

    @Override
    public void prepare() throws IOException {
        byte[] json = new JSONSerializer().deepSerialize(check).getBytes();
        getStream().writeInt(json.length);
        getStream().write(json);
        getStream().flush();
        getStream().close();

        RegionDataCheck.cache.remove(check);
    }
}
