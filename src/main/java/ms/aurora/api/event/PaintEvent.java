package ms.aurora.api.event;

import java.awt.*;

/**
 * Date: 21/06/13
 * Time: 09:39
 *
 * @author A_C/Cov
 */
public class PaintEvent {

    private Graphics2D graphics2D;

    public PaintEvent(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
    }

    public Graphics2D getGraphics() {
        return graphics2D;
    }
}
