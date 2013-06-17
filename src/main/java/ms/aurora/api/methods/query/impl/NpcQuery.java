package ms.aurora.api.methods.query.impl;

import ms.aurora.api.wrappers.NPC;
import ms.aurora.rt3.INpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ms.aurora.api.Context.getClient;

/**
 * Date: 23/05/13
 * Time: 10:46
 *
 * @author A_C/Cov
 */
public final class NpcQuery extends CharacterQuery<NPC, NpcQuery> {

    @Override
    public NPC[] result() {
        List<NPC> rsnpcList = new ArrayList<NPC>();
        for (INpc npc : getClient().getAllNpcs()) {
            if (npc != null) {
                rsnpcList.add(new NPC(npc));
            }
        }
        rsnpcList = filterResults(rsnpcList);
        Collections.sort(rsnpcList, DISTANCE_COMPARATOR);
        return rsnpcList.toArray(new NPC[rsnpcList.size()]);
    }

    public NpcQuery named(final String... names) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(NPC type) {
                for (String name : names) {
                    if (type.getName().equals(name)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    public NpcQuery id(final int... ids) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(NPC type) {
                for (int id : ids) {
                    if (type.getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }
}
