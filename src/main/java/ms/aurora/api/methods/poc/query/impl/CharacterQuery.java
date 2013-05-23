package ms.aurora.api.methods.poc.query.impl;

import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.poc.query.Query;
import ms.aurora.api.wrappers.Locatable;
import ms.aurora.api.wrappers.RSArea;
import ms.aurora.api.wrappers.RSCharacter;

/**
 * Date: 23/05/13
 * Time: 15:48
 *
 * @author A_C/Cov
 */
public abstract class CharacterQuery<RT extends RSCharacter, QT extends CharacterQuery> extends Query<RT, QT> {

    public QT distance(final int distance, final Locatable locatable) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                return Calculations.distance(type.getX(), type.getY(), locatable.getX(), locatable.getY()) < distance;
            }
        });
        return (QT) this;
    }

    public QT interacting(final RSCharacter character) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                return type.getInteracting().equals(character);
            }
        });
        return (QT) this;
    }

    public QT combat(final boolean inCombat) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                return type.isInCombat() && inCombat;
            }
        });
        return (QT) this;
    }

    public QT moving(final boolean isMoving) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                return type.isInCombat() && isMoving;
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

    public QT area(final RSArea area) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                return area.contains(type.getLocation());
            }
        });
        return (QT) this;
    }

    public QT animation(final int animation) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                return type.getAnimation() == animation;
            }
        });
        return (QT) this;
    }

}
