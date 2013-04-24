package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.rt3.Npc;

import java.util.ArrayList;
import java.util.List;

import static ms.aurora.api.Context.getClient;

/**
 * NPC related functions
 *
 * @author tobiewarburton
 * @author Rick
 */
public final class Npcs {

    /**
     * returns the closest {@link RSNPC} which matches the given predicates
     *
     * @param predicates the {@link Predicate} in which are required to be satisfied
     * @return the closest {@link RSNPC} which satisfies the predicates if there is one or null
     * @see RSNPC#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public static RSNPC get(final Predicate<RSNPC>... predicates) {
        return getClosest(ArrayUtils.filter(getAll(), predicates));
    }

    /**
     * return an array of all the {@link RSNPC} which match any of the given predicates
     *
     * @param predicates a var-args array of {@link Predicate} in which is required to be satisfied
     * @return an array of the {@link RSNPC} which satisfy the given predicates
     * @see Predicate
     */
    public static RSNPC[] getAll(final Predicate<RSNPC>... predicates) {
        return ArrayUtils.filter(getAll(), predicates);
    }


    /**
     * returns a list of all the {@link RSNPC} that are loaded in the client which aren't null
     *
     * @return a list of all the {@link RSNPC} that aren't null which are loaded into the client
     */
    public static RSNPC[] getAll() {
        List<RSNPC> validNPCs = new ArrayList<RSNPC>();
        for(Npc npc : getClient().getAllNpcs()) {
            if(npc != null) {
                validNPCs.add(new RSNPC(Context.get(), npc));
            }
        }
        return validNPCs.toArray(new RSNPC[validNPCs.size()]);
    }

    /**
     * Gets the closest NPC out of an array of NPCs
     *
     * @param npcs NPC array
     * @return closest NPC
     */
    private static RSNPC getClosest(RSNPC[] npcs) {
        RSNPC closest = null;
        int closestDistance = 9999;
        for (RSNPC npc : npcs) {
            int distance = npc.distance(Players.getLocal());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = npc;
            }
        }
        return closest;
    }
}
