package ms.aurora.api.util;

/**
 * @author Rick
 */
public interface Predicate<T> {

    boolean apply(T object);

}
