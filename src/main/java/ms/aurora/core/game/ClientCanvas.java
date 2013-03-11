package ms.aurora.core.game;

import ms.aurora.api.drawing.Drawable;
import ms.aurora.core.Session;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author rvbiljouw
 */
public class ClientCanvas extends Canvas {
    private static final long serialVersionUID = 4392449009242794406L;
    private final BufferedImage backBuffer =
            new BufferedImage(765, 503, BufferedImage.TYPE_INT_RGB);

    public ClientCanvas() {
        super();
    }

    @Override
    public Graphics getGraphics() {
        Graphics _super = super.getGraphics();
        Graphics2D graphics2D = backBuffer.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Session session = Session.lookupSession(getParent().hashCode());
        if (session != null) {
            for (Drawable drawable : session.getDrawables()) {
                drawable.draw(graphics2D);
            }
        }
        _super.drawImage(backBuffer, 0, 0, null);
        return backBuffer.getGraphics();
    }
}
