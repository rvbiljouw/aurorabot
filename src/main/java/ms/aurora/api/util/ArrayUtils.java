package ms.aurora.api.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A class presenting some simple utility functions
 * for interacting with/on arrays.
 * @author rvbiljouw
 */
public class ArrayUtils {

    /**
     * Predicates an array based on a set of predicates.
     * @param items Array to filter
     * @param predicate varargs set of predicates
     * @param <T> A type parameter indicating what type
     *           of items we're filtering.
     * @return a list of filtered items.
     */
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

    /**
     * Counts all matching elements in the specified array.
     * @param items Array to count items in
     * @param predicate varargs set of predicates
     * @param <T> A type parameter indicating what type
     *           of items we're counting.
     * @return amount of items in \items\ matching \predicate\
     */
    public static <T> int count(T[] items, Predicate<T>... predicate) {
        return filter(items, predicate).size();
    }

    /**
     * Counts all matching elements in the specified array.
     * @param items Array to count items in
     * @param predicate varargs set of predicates
     * @param <T> A type parameter indicating what type
     *           of items we're counting.
     * @return true if the returned count exceeds 0.
     */
    public static <T> boolean contains(T[] items, Predicate<T>... predicate) {
        return count(items, predicate) > 0;
    }

    /**
     * Transforms objects from one type to another
     * using a specified transform function.
     * @param items Items to transform
     * @param transform The transform to use
     * @param <F> Primary type
     * @param <T> Secondary type.
     * @return  List of secondary type objects.
     */
    public static <F, T> List<T> map(F[] items, Transform<F, T> transform) {
        List<T> transformed = new ArrayList<T>();
        for(F item : items) {
            T conv = transform.transform(item);
            if(conv != null) {
                transformed.add(conv);
            }
        }
        return transformed;
    }

}
