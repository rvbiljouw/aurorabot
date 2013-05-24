package ms.aurora.api.methods.query.impl;

import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.query.Query;
import ms.aurora.api.wrappers.Locatable;
import ms.aurora.api.wrappers.RSArea;
import ms.aurora.api.wrappers.RSCharacter;

/**
 * Date: 23/05/13
 * Time: 15:48
 *
 * @author A_C/Cov
 */
public abstract class CharacterQuery<RT extends RSCharacter, QT extends CharacterQuery> extends LocatableQuery<RT, QT> {

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
