import ms.aurora.api.Objects;
import ms.aurora.api.script.LoopScript;
import ms.aurora.api.script.ScriptMetadata;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSObject;

import java.awt.*;

/**
 * @author rvbiljouw
 */
@ScriptMetadata(name = "ObjectDebug")
public class ObjectDebug extends LoopScript {
    @Override
    public int loop() {
        return 600;
    }

    @Override
    public void draw(Graphics2D graphics) {
        RSObject[] objects = Objects.getAll(new Predicate<RSObject>() {
            @Override
            public boolean apply(RSObject object) {
                return object.distance(getMyPlayer()) < 5;
            }
        });

        for(RSObject object : objects) {
            Point loc =object.getScreenLocation();
            graphics.drawString(String.valueOf(object.getId()), loc.x, loc.y);
        }
    }
}
