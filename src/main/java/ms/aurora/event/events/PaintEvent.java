package ms.aurora.event.events;

import ms.aurora.event.GameEvent;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;
import java.util.EventListener;

/**
 * @author Rick
 */
public final class PaintEvent extends GameEvent<Graphics> {

    public PaintEvent(Graphics object) {
        super(object, PaintListener.class);
    }

    @Override
    public void dispatch(EventListener e) {
        ((PaintListener) e).onRepaint(getObject());
    }
}
