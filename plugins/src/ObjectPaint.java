import ms.aurora.api.ClientContext;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class ObjectPaint implements PaintListener {
    private final ClientContext ctx;

    public ObjectPaint(ClientContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onRepaint(Graphics graphics) {
        RSObject[] objects = ctx.objects.getAll(RSOBJECT_PREDICATE);
        for(RSObject object : objects) {
            Point loc = object.getScreenLocation();
            graphics.drawString(String.valueOf(object.getId()), loc.x, loc.y);
        }
    }

    private final Predicate<RSObject> RSOBJECT_PREDICATE = new Predicate<RSObject>() {
        @Override
        public boolean apply(RSObject object) {
            return object.distance(ctx.players.getLocal()) < 10;
        }
    };
}
