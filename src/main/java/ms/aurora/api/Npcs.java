package ms.aurora.api;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import ms.aurora.api.rt3.Npc;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;

import java.util.Collection;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static ms.aurora.api.ClientContext.context;

public class Npcs {

    public static RSNPC get(final Predicate<RSNPC> predicate) {
        return getClosest(Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSNPC>() {
                    @Override
                    public boolean apply(RSNPC object) {
                        return predicate.apply(object);
                    }
                }).toArray(new RSNPC[0]));
    }

    private static RSNPC getClosest(RSNPC[] npcs) {
        RSNPC closest = null;
        int closestDistance = 9999;
        for (RSNPC npc : npcs) {
            int distance = npc.distance(context.get().getMyPlayer());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = npc;
            }
        }
        return closest;
    }

    public static RSNPC[] getAll() {
        return _getAll().toArray(new RSNPC[0]);
    }

    private static Collection<RSNPC> _getAll() {
        return filter(transform(newArrayList(context.get().getClient()
                .getAllNpcs()), transform), Predicates.notNull());
    }

    private static final Function<Npc, RSNPC> transform = new Function<Npc, RSNPC>() {
        @Override
        public RSNPC apply(Npc npc) {
            return new RSNPC(context.get(), npc);
        }
    };
}
