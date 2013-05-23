package ms.aurora.api.methods.poc.query;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Date: 23/05/13
 * Time: 10:39
 *
 * @author A_C/Cov
 * BT = Base Type, eg Npc
 * RT = Return Type, eg RSNPC
 * QT = Query Type, eg NpcQuery
 */
public abstract class Query<BT, RT, QT extends Query> {

    public abstract class Conditional {
        protected abstract boolean accept(BT type);
    }

    private ArrayList<Conditional> executables = new ArrayList<>();
    protected Sort.Type sortType = Sort.Type.DEFAULT;
    protected Comparator comparator;

    protected void addExecutable(Conditional executable) {
        this.executables.add(executable);
    }

    protected List<BT> filterResults(BT[] items) {
        List<BT> filtered = new ArrayList();
        for (BT item: items) {
            boolean valid = true;

            for (Conditional p : executables) {
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
        this.executables.add(conditional);
        return (QT) this;
    }

    public RT single() {
        return result()[0];
    }

    public abstract RT[] result();

}
