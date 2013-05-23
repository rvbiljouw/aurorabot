package ms.aurora.api.methods.poc.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 23/05/13
 * Time: 10:39
 *
 * @author A_C/Cov
 */
public abstract class Query<T, R> {

    protected abstract class Executable {
        protected abstract boolean accept(T type);
    }

    private ArrayList<Executable> executables = new ArrayList<>();
    protected Sort.Type sortType = Sort.Type.DEFAULT;

    protected void addExecutable(Executable executable) {
        this.executables.add(executable);
    }

    protected List<T> filter(T[] items) {
        List<T> filtered = new ArrayList();
        for (T item: items) {
            boolean valid = true;

            for (Executable p : executables) {
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

    public Query sort(Sort.Type type) {
        this.sortType = type;
        return this;
    }

    public R single() {
        return result()[0];
    }

    public abstract R[] result();



}
