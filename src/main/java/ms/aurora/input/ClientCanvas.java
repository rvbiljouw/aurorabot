package ms.aurora.input;

import ms.aurora.api.ClientContext;
import ms.aurora.core.Session;
import ms.aurora.core.SessionRepository;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author rvbiljouw
 */
public class ClientCanvas extends Canvas {
    private static final long serialVersionUID = 4392449009242794406L;
    private final BufferedImage backBuffer = new BufferedImage(765, 503,
            BufferedImage.TYPE_INT_ARGB);
    private final BufferedImage botBuffer = new BufferedImage(765, 503,
            BufferedImage.TYPE_INT_ARGB);
    private final ClientContext context = new ClientContext();
    private Session session;

    public ClientCanvas() {
        super();
        ClientContext.set(context);
    }

    @Override
    public Graphics getGraphics() {
        Graphics _super = super.getGraphics();
        Graphics2D graphics2D = (Graphics2D) botBuffer.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(backBuffer, 0, 0, null);
        dispatchEvent(graphics2D);
        if (_super != null) {
            _super.drawImage(botBuffer, 0, 0, null);
        }
        return backBuffer.getGraphics();
    }

    private void dispatchEvent(Graphics g) {
        if (session == null) {
            session = SessionRepository.get(getParent().hashCode());
            context.setSession(session);
        } else {
            session.getPaintManager().onRepaint(g);
        }
    }
}
