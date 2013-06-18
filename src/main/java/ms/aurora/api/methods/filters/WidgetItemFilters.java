package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.WidgetItem;

/**
 * A class containing several filters for WidgetItem
 *
 * @author Matty Cov
 * @author tobiewarburton
 */
public final class WidgetItemFilters {

    private WidgetItemFilters() {

    }

    /**
     * a predicate helper which creates a predicate for a widget item which
     * is for one of the specified ids
     *
     * @param ids the id's in which you want the widget item to be
     * @return a predicate which filters by the specified id's
     * @see ms.aurora.api.wrappers.WidgetItem#getId()
     */
    public static Predicate<WidgetItem> ID(final int... ids) {
        return new Predicate<WidgetItem>() {
            @Override
            public boolean apply(WidgetItem object) {
                for (int id : ids) {
                    if (object.getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * @param size the size of the stack
     * @return a list of the widget items which have a stack size greater than the specified
     * @see ms.aurora.api.wrappers.WidgetItem#getStackSize()
     */
    public static Predicate<WidgetItem> STACKSIZE_GREATER_THAN(final int size) {
        return new Predicate<WidgetItem>() {
            @Override
            public boolean apply(WidgetItem object) {
                return object.getStackSize() > size;
            }
        };
    }

    /**
     * @param size the size of the stack
     * @return a list of the widget items which have a stack size less than the specified
     * @see ms.aurora.api.wrappers.WidgetItem#getStackSize()
     */
    public static Predicate<WidgetItem> STACKSIZE_LESS_THAN(final int size) {
        return new Predicate<WidgetItem>() {
            @Override
            public boolean apply(WidgetItem object) {
                return object.getStackSize() < size;
            }
        };
    }

}
