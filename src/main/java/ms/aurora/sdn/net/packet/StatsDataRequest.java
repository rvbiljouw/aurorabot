package ms.aurora.sdn.net.packet;

import flexjson.JSONSerializer;
import ms.aurora.api.Context;
import ms.aurora.core.Session;
import ms.aurora.sdn.net.OutgoingPacket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author iJava
 */
public class StatsDataRequest extends OutgoingPacket {

    @Override
    public int getOpcode() {
        return 9;
    }

    @Override
    public void prepare() throws IOException {
        String statsJson = new JSONSerializer().deepSerialize(Context.getSession().getStatusRepository().getSkills());
        getStream().writeUTF(statsJson);
        getStream().flush();
        getStream().close();
    }
}
