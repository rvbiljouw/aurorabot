package ms.aurora.api.methods.query.impl;

import ms.aurora.api.wrappers.Player;
import ms.aurora.rt3.IPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ms.aurora.api.Context.getClient;

/**
 * Date: 24/05/13
 * Time: 08:38
 *
 * @author A_C/Cov
 */
public final class PlayerQuery extends CharacterQuery<Player, PlayerQuery> {
    @Override
    public Player[] result() {
        List<Player> rsPlayers = new ArrayList<Player>();
        for (IPlayer player: getClient().getAllPlayers()) {
            if (player != null) {
                rsPlayers.add(new Player(player));
            }
        }
        rsPlayers = filterResults(rsPlayers);
        Collections.sort(rsPlayers, DISTANCE_COMPARATOR);
        return rsPlayers.toArray(new Player[rsPlayers.size()]);
    }

    public Player local() {
        return new Player(getClient().getLocalPlayer());
    }

    public PlayerQuery name(final String... names) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(Player type) {
                for (String name: names) {
                    if (name.equals(type.getName())) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

}
