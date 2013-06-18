package ms.aurora.api.methods;

import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.NPC;
import ms.aurora.rt3.INpc;

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
     * returns the closest {@link ms.aurora.api.wrappers.NPC} which matches the given predicates
     *
     * @param predicates the {@link Predicate} in which are required to be satisfied
     * @return the closest {@link ms.aurora.api.wrappers.NPC} which satisfies the predicates if there is one or null
     * @see ms.aurora.api.wrappers.NPC#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public static NPC get(final Predicate<NPC>... predicates) {
        return getClosest(ArrayUtils.filter(getAll(), predicates).toArray(new NPC[0]));
    }

    /**
     * return an array of all the {@link ms.aurora.api.wrappers.NPC} which match any of the given predicates
     *
     * @param predicates a var-args array of {@link Predicate} in which is required to be satisfied
     * @return an array of the {@link ms.aurora.api.wrappers.NPC} which satisfy the given predicates
     * @see Predicate
     */
    public static NPC[] getAll(final Predicate<NPC>... predicates) {
        return ArrayUtils.filter(getAll(), predicates).toArray(new NPC[0]);
    }


    /**
     * returns a list of all the {@link ms.aurora.api.wrappers.NPC} that are loaded in the client which aren't null
     *
     * @return a list of all the {@link ms.aurora.api.wrappers.NPC} that aren't null which are loaded into the client
     */
    public static NPC[] getAll() {
        List<NPC> validNPCs = new ArrayList<NPC>();
        for(INpc npc : getClient().getAllNpcs()) {
            if(npc != null) {
                validNPCs.add(new NPC(npc));
            }
        }
        return validNPCs.toArray(new NPC[validNPCs.size()]);
    }

    /**
     * Gets the closest NPC out of an array of NPCs
     *
     * @param npcs NPC array
     * @return closest NPC
     */
    private static NPC getClosest(NPC[] npcs) {
        NPC closest = null;
        int closestDistance = 9999;
        for (NPC npc : npcs) {
            int distance = npc.distance(Players.getLocal());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = npc;
            }
        }
        return closest;
    }
}
