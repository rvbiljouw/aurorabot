package ms.aurora.api.plugin.internal;

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
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class WallObjectPaint implements PaintListener {

    @Override
    public void onRepaint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        RSObject[] objects = Objects.getAll(RSOBJECT_PREDICATE);
        for (RSObject object : objects) {
            Point loc = object.getScreenLocation();
            graphics.setColor(Color.RED);
            graphics.drawString(String.valueOf(object.getId()), loc.x, loc.y);
        }
    }

    private final static Predicate<RSObject> RSOBJECT_PREDICATE = new Predicate<RSObject>() {
        @Override
        public boolean apply(RSObject object) {
            return object.distance(Players.getLocal()) < 7
                    && object.getObjectType().equals(RSObject.ObjectType.WALL_OBJECT);
        }
    };

}
