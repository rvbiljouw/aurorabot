package ms.aurora.api.methods;

import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.Player;
import ms.aurora.rt3.IPlayer;

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
     * gets the local {@link ms.aurora.api.wrappers.Player}
     *
     * @return the local {@link ms.aurora.api.wrappers.Player}
     */
    public static Player getLocal() {
        return new Player(getClient().getLocalPlayer());
    }

    /**
     * returns the closest {@link ms.aurora.api.wrappers.Player} which matches the given predicates
     *
     * @param predicates the {@link Predicate} in which are required to be satisfied
     * @return the closest {@link ms.aurora.api.wrappers.Player} which satisfies the predicates if there is one or null
     * @see ms.aurora.api.wrappers.Player#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public static Player get(final Predicate<Player>... predicates) {
        return getClosest(ArrayUtils.filter(getAll(), predicates).toArray(new Player[0]));
    }

    /**
     * return an array of all the {@link ms.aurora.api.wrappers.Player} which match any of the given predicates
     *
     * @param predicates a var-args array of {@link Predicate} in which is required to be satisfied
     * @return an array of the {@link ms.aurora.api.wrappers.Player} which satisfy the given predicates
     * @see Predicate
     */
    public static Player[] getAll(final Predicate<Player>... predicates) {
        return ArrayUtils.filter(getAll(), predicates).toArray(new Player[0]);
    }

    /**
     * Gets all players loaded in the client.
     *
     * @return array of loaded players.
     */
    public static Player[] getAll() {
        List<Player> players = new ArrayList<Player>();
        for (IPlayer player: getClient().getAllPlayers()) {
            if (player != null) {
                players.add(new Player(player));
            }
        }
        return players.toArray(new Player[players.size()]);
    }

    /**
     * Gets the closest RSPlayer out of an array of RSPlayers
     *
     * @param players RSPlayer array
     * @return closest RSPlayer
     */
    private static Player getClosest(Player[] players) {
        Player closest = null;
        int closestDistance = 9999;
        for (Player player : players) {
            int distance = player.distance(Players.getLocal());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = player;
            }
        }
        return closest;
    }

}
