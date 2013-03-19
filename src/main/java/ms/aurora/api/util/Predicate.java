package ms.aurora.api.util;

/**
 * @author rvbiljouw
 */
public interface Predicate<T> {

    boolean apply(T object);

}
