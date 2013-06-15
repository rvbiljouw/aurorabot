package ms.aurora.api.methods.query;

import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Players;
import ms.aurora.api.wrappers.Locatable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author A_C/Cov
 * @author tobiewarburton
 *
 * RT = Return Type, eg RSNPC
 */
public abstract class Query<RT> {

    /**
     * a simple condition
     */
    public abstract class Conditional {
        protected abstract boolean accept(RT type);
    }

    private ArrayList<Conditional> conditionals = new ArrayList<Conditional>();
    
    /**
     * a Comparator by distance
     */
    protected static final Comparator DISTANCE_COMPARATOR = new Comparator<Locatable>() {
        @Override
        public int compare(Locatable o1, Locatable o2) {
            return (int) Calculations.distance(o1.getLocation(), Players.getLocal().getLocation())
                    - (int)Calculations.distance(o2.getLocation(), Players.getLocal().getLocation());
        }
    };;

    /**
     * adds a conditional to the list
     *
     * @param conditional the conditional to add
     */
    protected void addConditional(Conditional conditional) {
        this.conditionals.add(conditional);
    }

    /**
     * filters the list provided by the conditionals.
     *
     * @param items a list of items to filter.
     */
    protected List<RT> filterResults(List<RT> items) {
        List<RT> filtered = new ArrayList<RT>();
        for (RT item: items) {
            boolean valid = true;
            for (Conditional p : conditionals) {
                if (!p.accept(item)) {
                    valid = false;
                }
            }

            if(valid) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    /**
     * @return the head of the list or null
     */
    public RT single() {
        RT[] result = result();
        return result.length > 0 ? result[0] : null;
    }

    /**
     * @return a list of items which are filtered by the conditionals
     */
    public abstract RT[] result();

}
