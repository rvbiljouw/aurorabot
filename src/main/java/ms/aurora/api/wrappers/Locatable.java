package ms.aurora.api.wrappers;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public interface Locatable {

    public Point getScreenLocation();

    public RSTile getLocation();

    public RSTile getRegionalLocation();

    public int getX();

    public int getY();

}
