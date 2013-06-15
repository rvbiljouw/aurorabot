package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSPlayer;
import ms.aurora.rt3.Player;

import java.util.ArrayList;
import java.util.List;

import static ms.aurora.api.Context.getClient;

/**
 * Player related functions
 *
 * @author Rick
 * @author tobiewarburton
 */
public final class Players {

    /**
     * gets the local {@link RSPlayer}
     *
     * @return the local {@link RSPlayer}
     */
    public static RSPlayer getLocal() {
        return new RSPlayer(getClient().getLocalPlayer());
    }

    /**
     * returns the closest {@link RSPlayer} which matches the given predicates
     *
     * @param predicates the {@link Predicate} in which are required to be satisfied
     * @return the closest {@link RSPlayer} which satisfies the predicates if there is one or null
     * @see RSPlayer#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public static RSPlayer get(final Predicate<RSPlayer>... predicates) {
        return getClosest(ArrayUtils.filter(getAll(), predicates).toArray(new RSPlayer[0]));
    }

    /**
     * return an array of all the {@link RSPlayer} which match any of the given predicates
     *
     * @param predicates a var-args array of {@link Predicate} in which is required to be satisfied
     * @return an array of the {@link RSPlayer} which satisfy the given predicates
     * @see Predicate
     */
    public static RSPlayer[] getAll(final Predicate<RSPlayer>... predicates) {
        return ArrayUtils.filter(getAll(), predicates).toArray(new RSPlayer[0]);
    }

    /**
     * Gets all players loaded in the client.
     *
     * @return array of loaded players.
     */
    public static RSPlayer[] getAll() {
        List<RSPlayer> players = new ArrayList<RSPlayer>();
        for (Player player: getClient().getAllPlayers()) {
            if (player != null) {
                players.add(new RSPlayer(player));
            }
        }
        return players.toArray(new RSPlayer[players.size()]);
    }

    /**
     * Gets the closest RSPlayer out of an array of RSPlayers
     *
     * @param players RSPlayer array
     * @return closest RSPlayer
     */
    private static RSPlayer getClosest(RSPlayer[] players) {
        RSPlayer closest = null;
        int closestDistance = 9999;
        for (RSPlayer player : players) {
            int distance = player.distance(Players.getLocal());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = player;
            }
        }
        return closest;
    }

}
