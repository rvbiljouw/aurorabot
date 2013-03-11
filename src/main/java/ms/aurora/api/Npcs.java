package ms.aurora.api;

import com.google.common.collect.Lists;
import ms.aurora.api.rt3.Npc;
import ms.aurora.api.wrappers.RSNpc;

import java.util.List;

public class Npcs {
    private final ClientContext context;

    public Npcs(ClientContext context) {
        this.context = context;
    }

    public RSNpc[] getAll() {
        List<RSNpc> rsNpcs = Lists.newArrayList();
        Npc[] npcs = context.getClient().getAllNpcs();
        for (int i = 0; i < npcs.length; i++) {
            if (npcs[i] != null) {
                rsNpcs.add(new RSNpc(context, npcs[i]));
            }
        }
        return rsNpcs.toArray(new RSNpc[0]);
    }
}
