package ms.aurora.api.wrappers;

import java.awt.*;

/**
 * @author Rick
 */
public interface Locatable {

    Point getScreenLocation();

    RSTile getLocation();

    RSTile getRegionalLocation();

    int getX();

    int getY();

}
