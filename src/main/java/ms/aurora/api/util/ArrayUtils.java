package ms.aurora.api.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rvbiljouw
 */
public class ArrayUtils {

    public static <T> List<T> filter(T[] items, Predicate<T>... predicate) {
        List<T> filtered = new ArrayList<T>();
        for (T item : items) {
            boolean valid = true;

            for (Predicate<T> p : predicate) {
                if (!p.apply(item)) {
                    valid = false;
                }
            }

            if(valid) {
                filtered.add(item);
            }
        }
        return filtered;
    }

}
