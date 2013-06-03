package ms.aurora.api.event;

import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.rt3.Npc;

/**
 * @author rvbiljouw
 */
public class NpcSpawnEvent {
    private Npc npc;

    public NpcSpawnEvent(Npc npc) {
        this.npc = npc;
    }

    public RSNPC getNpc() {
        return npc == null ? null : new RSNPC(npc);
    }

}
