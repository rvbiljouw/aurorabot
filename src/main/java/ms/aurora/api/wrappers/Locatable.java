package ms.aurora.api.wrappers;

import java.awt.*;

/**
 * @author Rick
 */
public interface Locatable {

    Point getScreenLocation();

    Tile getLocation();

    Tile getRegionalLocation();

    int getX();

    int getY();

    boolean isOnScreen();

    boolean canReach();

}
