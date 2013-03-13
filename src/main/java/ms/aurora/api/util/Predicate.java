package ms.aurora.api.util;

/**
 * @author rvbiljouw
 */
public interface Predicate<T> {

    public boolean apply(T object);

}
