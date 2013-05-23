package ms.aurora.api.methods.poc.query.impl;

import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.poc.query.Query;
import ms.aurora.api.methods.poc.query.Sort;
import ms.aurora.api.wrappers.Locatable;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.rt3.Npc;

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
public class NpcQuery extends Query<Npc, RSNPC, NpcQuery> {

    @Override
    public RSNPC[] result() {
        Npc[] npcArray = filterResults(getClient().getAllNpcs()).toArray(new Npc[0]);
        List<RSNPC> rsnpcList = new ArrayList<>();
        for (Npc npc: npcArray) {
            rsnpcList.add(new RSNPC(npc));
        }
        if (this.comparator == null) {
            switch (sortType) {
                case DISTANCE:
                    comparator = Sort.DISTANCE;
                    break;
                default:
                    comparator = Sort.DEFAULT;
            }
        }
        Collections.sort(rsnpcList, comparator);
        return rsnpcList.toArray(new RSNPC[rsnpcList.size()]);
    }

    public NpcQuery name(final String... names) {
        this.addExecutable(new Conditional() {
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
        this.addExecutable(new Conditional() {
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

    public NpcQuery distance(final int distance, final Locatable locatable) {
        this.addExecutable(new Conditional() {
            @Override
            protected boolean accept(Npc type) {
                int x = getClient().getBaseX() + (type.getLocalX() >> 7);
                int y = getClient().getBaseY() + (type.getLocalY() >> 7);
                return Calculations.distance(x, y, locatable.getX(), locatable.getY()) < distance;
            }
        });
        return this;
    }

    public NpcQuery interacting(final boolean isInteracting) {
        this.addExecutable(new Conditional() {
            @Override
            protected boolean accept(Npc type) {
                int interacting = type.getInteractingEntity();
                if (interacting == -1) {
                    return !isInteracting;
                } else if (interacting < 32767) {
                    return (getClient().getAllNpcs()[interacting] != null) && isInteracting;
                } else if (interacting >= 32767) {
                    int index = (interacting - 32767);
                    return getClient().getAllPlayers()[index] != null && isInteracting;
                }
                return !isInteracting;
            }
        });
        return this;
    }

}
