package ms.aurora.api.methods.query.impl;

import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.query.Query;
import ms.aurora.api.wrappers.Locatable;
import ms.aurora.api.wrappers.RSArea;
import ms.aurora.api.wrappers.RSPlayer;
import ms.aurora.api.wrappers.RSTile;

/**
 * Date: 24/05/13
 * Time: 09:12
 *
 * @author A_C/Cov
 */
public abstract class LocatableQuery<RT extends Locatable, QT extends LocatableQuery> extends Query<RT, QT> {

    public QT location(final RSTile tile) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                return type.getLocation().equals(tile);
            }
        });
        return (QT) this;
    }

    public QT area(final RSArea area) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                return area.contains(type.getLocation());
            }
        });
        return (QT) this;
    }

    public QT onScreen() {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                return type.isOnScreen();
            }
        });
        return (QT) this;
    }

    public QT distance(final int distance) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                RSPlayer currentPlayer = new PlayerQuery().local();
                return Calculations.distance(type.getX(), type.getY(), currentPlayer.getX(), currentPlayer.getY()) < distance;
            }
        });
        return (QT) this;
    }

}
