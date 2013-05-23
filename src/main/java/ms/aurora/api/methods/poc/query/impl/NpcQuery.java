package ms.aurora.api.methods.poc.query.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.poc.query.Query;
import ms.aurora.api.methods.poc.query.Sort;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.rt3.Npc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Date: 23/05/13
 * Time: 10:46
 *
 * @author A_C/Cov
 */
public class NpcQuery extends Query<Npc, RSNPC> {
    @Override
    public RSNPC[] result() {
        Npc[] npcArray = filter(Context.getClient().getAllNpcs()).toArray(new Npc[0]);
        List<RSNPC> rsnpcList = new ArrayList<>();
        for (Npc npc: npcArray) {
            rsnpcList.add(new RSNPC(npc));
        }
        Comparator comparator = null;
        switch (sortType) {

            case DISTANCE:
                comparator = Sort.DISTANCE();
                break;
            default:
                comparator = Sort.DEFAULT();
        }
        Collections.sort(rsnpcList, comparator);
        return rsnpcList.toArray(new RSNPC[rsnpcList.size()]);
    }

    public NpcQuery name(final String... names) {
        this.addExecutable(new Executable() {
            @Override
            protected boolean accept(Npc type) {
                for (String name: names) {
                    if (type.getComposite().getName().equals(name)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    public NpcQuery id(final int... ids) {
        this.addExecutable(new Executable() {
            @Override
            protected boolean accept(Npc type) {
                for (int id: ids) {
                    if (type.getComposite().getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }
}
