package ms.aurora.event;

import ms.aurora.event.listeners.PaintListener;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Rick
 */
public final class PaintManager implements PaintListener {
    private static final Logger logger = Logger.getLogger(PaintManager.class);
    private final List<PaintListener> listeners = new ArrayList<PaintListener>();

    public void register(PaintListener listener) {
        listeners.add(listener);
    }

    public void deregister(PaintListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onRepaint(Graphics g) {
        for (PaintListener listener : listeners) {
            try {
                listener.onRepaint(g);
            } catch (Exception e) {
                logger.error("PaintListener threw exception", e);
            }
        }
    }
}
