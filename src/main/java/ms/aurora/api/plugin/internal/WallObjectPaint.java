package ms.aurora.api.plugin.internal;

import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Players;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.GameObject;
import ms.aurora.event.EventBus;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cov
 * Date: 24/04/13
 * Time: 22:26
 * To change this template use File | Settings | File Templates.
 */
public class WallObjectPaint {

    @EventBus.EventHandler
    public void onRepaint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        GameObject[] objects = Objects.getAll(RSOBJECT_PREDICATE);
        for (GameObject object : objects) {
            Point loc = object.getScreenLocation();
            graphics.setColor(Color.RED);
            graphics.drawString(String.valueOf(object.getId()), loc.x, loc.y);
        }
    }

    private final static Predicate<GameObject> RSOBJECT_PREDICATE = new Predicate<GameObject>() {
        @Override
        public boolean apply(GameObject object) {
            return object.distance(Players.getLocal()) < 7
                    && object.getObjectType().equals(GameObject.ObjectType.WALL_OBJECT);
        }
    };

}
