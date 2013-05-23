package ms.aurora.api.methods.poc.query;

import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Players;
import ms.aurora.api.wrappers.Locatable;

import java.util.Comparator;

/**
 * Date: 23/05/13
 * Time: 11:17
 *
 * @author A_C/Cov
 */
public class Sort {

    public enum Type {
        DEFAULT, DISTANCE
    }

    public static Comparator<?> DEFAULT() {
        return new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };
    }

    public static Comparator<Locatable> DISTANCE() {
        return new Comparator<Locatable>() {
            @Override
            public int compare(Locatable o1, Locatable o2) {
                return (int) Calculations.distance(o1.getLocation(), Players.getLocal().getLocation())
                        - (int)Calculations.distance(o2.getLocation(), Players.getLocal().getLocation());
            }
        };
    }

}
