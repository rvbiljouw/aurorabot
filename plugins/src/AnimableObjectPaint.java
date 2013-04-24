import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Players;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cov
 * Date: 24/04/13
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
public class AnimableObjectPaint implements PaintListener {
    @Override
    public void onRepaint(Graphics graphics) {
        RSObject[] objects = Objects.getAll(RSOBJECT_PREDICATE);
        for (RSObject object : objects) {
            Point loc = object.getScreenLocation();
            graphics.setColor(Color.YELLOW);
            graphics.drawString(String.valueOf(object.getId()), loc.x, loc.y);
            try {
                graphics.drawPolygon(object.getModel().getHull());
            } catch (Exception ignored) {}
        }
    }

    private final static Predicate<RSObject> RSOBJECT_PREDICATE = new Predicate<RSObject>() {
        @Override
        public boolean apply(RSObject object) {
            return object.distance(Players.getLocal()) < 5
                    && object.getObjectType().equals(RSObject.ObjectType.ANIMABLE);
        }
    };
}
