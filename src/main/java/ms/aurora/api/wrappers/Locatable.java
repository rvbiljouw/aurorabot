package ms.aurora.api.wrappers;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public interface Locatable {

    Point getScreenLocation();

    RSTile getLocation();

    RSTile getRegionalLocation();

    int getX();

    int getY();

}
