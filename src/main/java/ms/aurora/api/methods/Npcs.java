package ms.aurora.api.methods;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import ms.aurora.api.Context;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.rt3.Npc;

import java.util.Collection;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;

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
        return getClosest(Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSNPC>() {
                    @Override
                    public boolean apply(RSNPC object) {
                        for (Predicate<RSNPC> predicate : predicates) {
                            if (!predicate.apply(object)) return false;
                        }
                        return true;
                    }
                }).toArray(new RSNPC[0]));
    }

    /**
     * return an array of all the {@link RSNPC} which match the given predicate
     *
     * @param predicate the {@link Predicate} in which is required to be satisfied
     * @return an array of the {@link RSNPC} which satisfy the given predicate
     * @see Predicate
     */
    public static RSNPC[] getAll(final Predicate<RSNPC> predicate) {
        return Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSNPC>() {
                    @Override
                    public boolean apply(RSNPC object) {
                        return predicate.apply(object);
                    }
                }).toArray(new RSNPC[0]);
    }

    /**
     * return an array of all the {@link RSNPC} which match any of the given predicates
     *
     * @param predicates a var-args array of {@link Predicate} in which is required to be satisfied
     * @return an array of the {@link RSNPC} which satisfy the given predicates
     * @see Predicate
     */
    public static RSNPC[] getAll(final Predicate<RSNPC>... predicates) {
        return Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSNPC>() {
                    @Override
                    public boolean apply(RSNPC object) {
                        for(Predicate<RSNPC> pred : predicates) {
                            if(!pred.apply(object)) return false;
                        }
                        return true;
                    }
                }).toArray(new RSNPC[0]);
    }


    /**
     * returns a list of all the {@link RSNPC} that are loaded in the client which aren't null
     *
     * @return a list of all the {@link RSNPC} that aren't null which are loaded into the client
     */
    public static RSNPC[] getAll() {
        return _getAll().toArray(new RSNPC[0]);
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

    /**
     * Gets a collection of all NPCs in the region
     *
     * @return collection of NPCs
     */
    private static Collection<RSNPC> _getAll() {
        return filter(transform(newArrayList(Context.get().getClient()
                .getAllNpcs()), transform), Predicates.notNull());
    }

    /**
     * Transforms an unwrapped NPC into a wrapped one.
     */
    private static final Function<Npc, RSNPC> transform = new Function<Npc, RSNPC>() {
        @Override
        public RSNPC apply(Npc npc) {
            if (npc != null) {
                return new RSNPC(Context.get(), npc);
            }
            return null;
        }
    };
}
