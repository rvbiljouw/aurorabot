package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.core.Session;
import ms.aurora.core.SessionRepository;
import ms.aurora.event.listeners.SwapBufferListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static ms.aurora.api.Context.get;

/**
 * A canvas that will be extended by the RuneScape client.
 * This implementation allows us to intervene in the drawing process of the
 * client so that we may draw our own information on the client context.
 * The canvas is double-buffered, solving any flickering problems.
 *
 * @author Rick
 */
public class ClientCanvas extends Canvas {
    private static final long serialVersionUID = 4392449009242794406L;
    private final BufferedImage backBuffer = new BufferedImage(765, 503,
            BufferedImage.TYPE_INT_ARGB);
    private final BufferedImage botBuffer = new BufferedImage(765, 503,
            BufferedImage.TYPE_INT_ARGB);
    private Session session;

    public ClientCanvas() {
        super();
    }

    @Override
    public Graphics getGraphics() {
        Graphics _super = super.getGraphics();
        Graphics2D graphics2D = (Graphics2D) botBuffer.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(backBuffer, 0, 0, null);
        drawMouse(graphics2D);
        dispatchEvent(graphics2D);
        if (_super != null) {
            _super.drawImage(botBuffer, 0, 0, null);
        }
        return backBuffer.getGraphics();
    }

    private void dispatchEvent(Graphics g) {
        if (session == null) {
            session = SessionRepository.get(getParent().hashCode());
        } else {
            session.getPaintManager().onRepaint(g);
            session.getPaintManager().getSwapBufferListener().
                    onSwapBuffer(botBuffer);
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(0, 0, width, height);
    }

    private void drawMouse(Graphics2D g) {
        if (get() != null) {
            int mouseX = Context.getClient().getMouse().getRealX();
            int mouseY = Context.getClient().getMouse().getRealY();
            g.setColor(Color.YELLOW);
            g.drawLine(mouseX - 7, mouseY - 7, mouseX + 7, mouseY + 7);
            g.drawLine(mouseX + 7, mouseY - 7, mouseX - 7, mouseY + 7);

        }
    }
}
