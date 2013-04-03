package ms.aurora.event.listeners;

import java.awt.*;
import java.util.EventListener;

/**
 * @author Rick
 */
public interface PaintListener extends EventListener {

    /**
     * Called when a game frame is about to be drawn.
     * @param graphics The graphics object to draw on.
     */
    void onRepaint(Graphics graphics);

}
