package ms.aurora.api.methods.query.impl;

import ms.aurora.api.methods.query.Sort;
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
public class PlayerQuery extends CharacterQuery<RSPlayer, PlayerQuery> {
    @Override
    public RSPlayer[] result() {
        List<RSPlayer> rsPlayers = new ArrayList<RSPlayer>();
        for (Player player: getClient().getAllPlayers()) {
            if (player != null) {
                rsPlayers.add(new RSPlayer(player));
            }
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
        Collections.sort(filterResults(rsPlayers), comparator);
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
