package ms.aurora.core.input;

/**
 * @author tobiewarburton
 */
public interface KeyboardPredicate {
    /**
     * a simple method in which is used in the KeyboardProvider to wait until it is satisfied
     *
     * @return false to keep the key held, true to release the key
     */
    public boolean satisfied();
}
