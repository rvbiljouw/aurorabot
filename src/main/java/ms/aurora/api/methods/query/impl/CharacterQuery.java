package ms.aurora.api.methods.query.impl;

import ms.aurora.api.wrappers.Entity;

/**
 * Date: 23/05/13
 * Time: 15:48
 *
 * @author A_C/Cov
 */
public abstract class CharacterQuery<RT extends Entity, QT extends CharacterQuery> extends LocatableQuery<RT, QT> {

    public QT interacting(final Entity character) {
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
