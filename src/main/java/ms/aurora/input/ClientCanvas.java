package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.core.Repository;
import ms.aurora.core.Session;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ms.aurora.api.Context.getSession;


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
    public static int PAINT_DELAY = 10;
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
        graphics2D.drawImage(backBuffer, 0, 0, null);
        drawMouse(graphics2D);
        graphics2D.setColor(Color.RED);
        dispatchEvent(graphics2D);
        if (_super != null) {
            _super.drawImage(botBuffer, 0, 0, null);
        }
        Utilities.sleepNoException(PAINT_DELAY);
        return backBuffer.getGraphics();
    }

    private void dispatchEvent(Graphics g) {
        if (session == null) {
            session = Repository.get(getParent().hashCode());
        } else {
            session.getPaintManager().onRepaint(g);
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(0, 0, width, height);
    }

    private void drawMouse(Graphics2D g) {
        if (getSession() != null) {
            int mouseX = Context.getClient().getMouse().getRealX();
            int mouseY = Context.getClient().getMouse().getRealY();
            g.setColor(new Color(49, 110, 163, 150));
            g.fillRoundRect(mouseX - 9, mouseY - 3, 18, 6, 4, 4);
            g.fillRoundRect(mouseX - 3, mouseY - 9, 6, 18, 4, 4);
            g.setColor(Color.BLACK);
            g.drawRoundRect(mouseX - 9, mouseY - 3, 18, 6, 4, 4);
            g.drawRoundRect(mouseX - 3, mouseY - 9, 6, 18, 4, 4);
        }
    }
}
