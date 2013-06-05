package ms.aurora.sdn.net.packet;

import flexjson.JSONSerializer;
import ms.aurora.sdn.model.RegionDataPacket;
import ms.aurora.sdn.net.OutgoingPacket;

import java.io.IOException;
import java.util.List;

/**
 * @author tobiewarburton
 */
public class RegionData extends OutgoingPacket {
    private List<RegionDataPacket> checks;

    public RegionData(List<RegionDataPacket> checks) {
        this.checks = checks;
    }

    @Override
    public int getOpcode() {
        return 9;
    }

    @Override
    public void prepare() throws IOException {
        byte[] json = new JSONSerializer().deepSerialize(checks).getBytes("UTF-8");
        getStream().writeInt(json.length);
        getStream().write(json);
        getStream().flush();
        getStream().close();
    }
}
