package ms.aurora.api.methods.query;

import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Players;
import ms.aurora.api.wrappers.Locatable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Date: 23/05/13
 * Time: 10:39
 *
 * @author A_C/Cov
 *
 * RT = Return Type, eg RSNPC
 * QT = Query Type, eg NPCQuery
 */
public abstract class Query<RT> {

    public abstract class Conditional {
        protected abstract boolean accept(RT type);
    }

    private ArrayList<Conditional> conditionals = new ArrayList<Conditional>();
    protected static final Comparator DISTANCE_COMPARATOR = new Comparator<Locatable>() {
        @Override
        public int compare(Locatable o1, Locatable o2) {
            return (int) Calculations.distance(o1.getLocation(), Players.getLocal().getLocation())
                    - (int)Calculations.distance(o2.getLocation(), Players.getLocal().getLocation());
        }
    };;

    protected void addConditional(Conditional conditional) {
        this.conditionals.add(conditional);
    }

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

    public RT single() {
        RT[] result = result();
        return result.length > 0 ? result[0] : null;
    }

    public abstract RT[] result();

}
