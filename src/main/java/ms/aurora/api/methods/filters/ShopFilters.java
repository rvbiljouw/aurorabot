package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSWidgetItem;

/**
 * ms.aurora.api.methods.filters.ShopFilters
 * Created By: Matty Cov
 * Time: 11:26
 * Date: 14/05/13
 */
public class ShopFilters {

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

}
