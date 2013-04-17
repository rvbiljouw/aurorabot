package ms.aurora.poc;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSObject;

/**
 * @author rvbiljouw
 */
public interface DeferredSelectionListener {

    public void selectObject(RSObject object);

    public RSObject getSelectedObject();

    public Predicate<RSObject> getPredicate();

}
