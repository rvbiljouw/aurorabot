package ms.aurora.api.methods.poc.query.impl;

import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.poc.query.Query;
import ms.aurora.api.wrappers.Locatable;
import ms.aurora.api.wrappers.RSCharacter;

/**
 * Date: 23/05/13
 * Time: 15:48
 *
 * @author A_C/Cov
 */
public abstract class CharacterQuery<RT extends RSCharacter, QT extends CharacterQuery> extends Query<RT, QT> {

    public QT distance(final int distance, final Locatable locatable) {
        this.addExecutable(new Conditional() {
            @Override
            protected boolean accept(RSCharacter type) {
                return Calculations.distance(type.getX(), type.getY(), locatable.getX(), locatable.getY()) < distance;
            }
        });
        return (QT) this;
    }

    public QT interacting(final RSCharacter character) {
        this.addExecutable(new Conditional() {
            @Override
            protected boolean accept(RSCharacter type) {
                return type.getInteracting().equals(character);
            }
        });
        return (QT) this;
    }

    public QT combat(final boolean inCombat) {
        this.addExecutable(new Conditional() {
            @Override
            protected boolean accept(RSCharacter type) {
                return type.isInCombat() && inCombat;
            }
        });
        return (QT) this;
    }

    public QT moving(final boolean isMoving) {
        this.addExecutable(new Conditional() {
            @Override
            protected boolean accept(RSCharacter type) {
                return type.isInCombat() && isMoving;
            }
        });
        return (QT) this;
    }

    public QT onScreen() {
        this.addExecutable(new Conditional() {
            @Override
            protected boolean accept(RT type) {
                return type.isOnScreen();
            }
        });
        return (QT) this;
    }

}
