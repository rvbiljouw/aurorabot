package ms.aurora.api.methods.filters;

import ms.aurora.api.methods.web.model.World;
import ms.aurora.api.util.Predicate;

/**
 * @author rvbiljouw
 */
public class WorldFilters {

    public static Predicate<World> ID(final int worldNumber) {
        return new Predicate<World>() {
            @Override
            public boolean apply(World object) {
                return object.getWorldNo() == worldNumber;
            }
        };
    }

    public static Predicate<World> COUNTRY(final String countryName) {
        return new Predicate<World>() {
            @Override
            public boolean apply(World object) {
                return object.getCountry().toLowerCase().contains(countryName.toLowerCase());
            }
        };
    }

}
