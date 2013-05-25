package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.core.Repository;
import ms.aurora.core.Session;
import ms.aurora.gui.Icons;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
    private static final Color MOUSE_BLUE = new Color(65, 169, 201);
    private static final Color MOUSE_GREY = new Color(236, 236, 236);

    public static int PAINT_DELAY = 10;
    private final BufferedImage backBuffer = new BufferedImage(765, 503,
            BufferedImage.TYPE_INT_ARGB);
    private final BufferedImage botBuffer = new BufferedImage(765, 503,
            BufferedImage.TYPE_INT_ARGB);
    private BufferedImage MOUSE_POINTER;
    private Session session;

    public ClientCanvas() {
        super();
        try {
            MOUSE_POINTER = ImageIO.read(Icons.class.getResourceAsStream("icons/mouse.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

            if(MOUSE_POINTER != null) {
                g.drawImage(MOUSE_POINTER, mouseX, mouseY, null);
            }


            /*g.setColor(MOUSE_GREY);
            g.drawLine(mouseX, 0, mouseX, mouseY - 10);
            g.drawLine(mouseX, mouseY + 10, mouseX, 503);
            g.drawLine(0, mouseY, mouseX - 10, mouseY);
            g.drawLine(mouseX + 10, mouseY, 765, mouseY);

            g.setColor(MOUSE_BLUE);
            g.drawRect(mouseX - 10, mouseY - 10, 20, 20);

            g.setColor(MOUSE_GREY);
            g.drawRect(mouseX - 1, mouseY - 1, 3, 3);   */
        }
    }
}
