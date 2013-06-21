package ms.aurora.api.plugin.internal;

import ms.aurora.api.event.EventBus;
import ms.aurora.api.event.PaintEvent;
import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Players;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.GameObject;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cov
 * Date: 24/04/13
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
public class AnimableObjectPaint {

    @EventBus.EventHandler
    public void onRepaint(PaintEvent event) {
        Graphics2D graphics = event.getGraphics();
        GameObject[] objects = Objects.getAll(RSOBJECT_PREDICATE);
        for (GameObject object : objects) {
            Point loc = object.getScreenLocation();
            graphics.setColor(Color.YELLOW);
            graphics.drawString(String.valueOf(object.getId()), loc.x, loc.y);
        }
    }

    private final static Predicate<GameObject> RSOBJECT_PREDICATE = new Predicate<GameObject>() {
        @Override
        public boolean apply(GameObject object) {
            return object.distance(Players.getLocal()) < 5
                    && object.getObjectType().equals(GameObject.ObjectType.ANIMABLE);
        }
    };
}
