package ms.aurora.api.methods.query.impl;

import ms.aurora.api.wrappers.RSPlayer;
import ms.aurora.rt3.Player;

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
public final class PlayerQuery extends CharacterQuery<RSPlayer, PlayerQuery> {
    @Override
    public RSPlayer[] result() {
        List<RSPlayer> rsPlayers = new ArrayList<RSPlayer>();
        for (Player player: getClient().getAllPlayers()) {
            if (player != null) {
                rsPlayers.add(new RSPlayer(player));
            }
        }
        rsPlayers = filterResults(rsPlayers);
        Collections.sort(rsPlayers, DISTANCE_COMPARATOR);
        return rsPlayers.toArray(new RSPlayer[rsPlayers.size()]);
    }

    public RSPlayer local() {
        return new RSPlayer(getClient().getLocalPlayer());
    }

    public PlayerQuery name(final String... names) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RSPlayer type) {
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
