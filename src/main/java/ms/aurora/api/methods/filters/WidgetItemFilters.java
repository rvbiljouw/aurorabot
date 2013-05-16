package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSWidgetItem;

/**
 * ms.aurora.api.methods.filters.WidgetItemFilters
 * Created By: Matty Cov
 * Time: 11:26
 * Date: 14/05/13
 */
public class WidgetItemFilters {

    public static Predicate<RSWidgetItem> ID(final int... ids) {
        return new Predicate<RSWidgetItem>() {
            @Override
            public boolean apply(RSWidgetItem object) {
                for (int id: ids) {
                    if (object.getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Predicate<RSWidgetItem> STACKSIZE_GREATER_THAN(final int size) {
        return new Predicate<RSWidgetItem>() {
            @Override
            public boolean apply(RSWidgetItem object) {
                return object.getStackSize() > size;
            }
        };
    }

    public static Predicate<RSWidgetItem> STACKSIZE_LESS_THAN(final int size) {
        return new Predicate<RSWidgetItem>() {
            @Override
            public boolean apply(RSWidgetItem object) {
                return object.getStackSize() < size;
            }
        };
    }

}
