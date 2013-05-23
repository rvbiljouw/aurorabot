package ms.aurora.api.methods.poc.query;

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
 * QT = Query Type, eg NpcQuery
 */
public abstract class Query<RT, QT extends Query> {

    public abstract class Conditional {
        protected abstract boolean accept(RT type);
    }

    private ArrayList<Conditional> conditionals = new ArrayList<>();
    protected Sort.Type sortType = Sort.Type.DEFAULT;
    protected Comparator comparator;

    protected void addConditional(Conditional conditional) {
        this.conditionals.add(conditional);
    }

    protected List<RT> filterResults(List<RT> items) {
        List<RT> filtered = new ArrayList();
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

    public QT sort(Sort.Type type) {
        sortType = type;
        return (QT) this;
    }

    public QT sort(Comparator comparator) {
        this.sortType = Sort.Type.DEFAULT;
        this.comparator = comparator;
        return (QT) this;
    }

    public QT filter(Conditional conditional) {
        this.conditionals.add(conditional);
        return (QT) this;
    }

    public RT single() {
        return result()[0];
    }

    public abstract RT[] result();

}
