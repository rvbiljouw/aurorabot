package ms.aurora.event;

import ms.aurora.core.Session;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author rvbiljouw
 */
public class PaintManager implements PaintListener {
    private final List<PaintListener> listeners = newArrayList();
    private final Session session;

    public PaintManager(Session session) {
        this.session = session;
    }

    public void register(PaintListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onRepaint(Graphics g) {
        for(PaintListener listener : listeners) {
            listener.onRepaint(g);
        }
    }
}
