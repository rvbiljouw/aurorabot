package ms.aurora.api.util;

/**
 * A transform converts objects from one type to another.
 * @author rvbiljouw
 */
public interface Transform<F, T> {

    /**
     * Transforms object of type F into type T.
     * @param object object to transform
     * @return transformed object.
     */
    public T transform(F object);

}
