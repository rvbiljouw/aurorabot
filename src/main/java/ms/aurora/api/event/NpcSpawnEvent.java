package ms.aurora.api.event;

import ms.aurora.api.wrappers.NPC;
import ms.aurora.rt3.INpc;

/**
 * @author rvbiljouw
 */
public class NpcSpawnEvent {
    private INpc npc;

    public NpcSpawnEvent(INpc npc) {
        this.npc = npc;
    }

    public NPC getNpc() {
        return npc == null ? null : new NPC(npc);
    }

}
