package ms.aurora.input;

import ms.aurora.api.drawing.Drawable;
import ms.aurora.core.Session;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * @author rvbiljouw
 */
public class ClientCanvas extends Canvas implements MouseMotionListener,
        MouseListener {
    private static final long serialVersionUID = 4392449009242794406L;
    private final BufferedImage backBuffer = new BufferedImage(765, 503,
            BufferedImage.TYPE_INT_ARGB);
    private final BufferedImage botBuffer = new BufferedImage(765, 503,
            BufferedImage.TYPE_INT_ARGB);
    private Session session;
    private int mouseX;
    private int mouseY;
    private int lastClickX;
    private int lastClickY;
    private long lastPressTime;

    public ClientCanvas() {
        super();
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    public Graphics getGraphics() {
        Graphics _super = super.getGraphics();
        Graphics2D graphics2D = (Graphics2D) botBuffer.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(backBuffer, 0, 0, null);

        drawSession(graphics2D);
        drawMouse(graphics2D);

        if (_super != null) {
            _super.drawImage(botBuffer, 0, 0, null);
        }
        return backBuffer.getGraphics();
    }

    private void drawSession(Graphics2D g) {
        if (session == null) {
            session = Session.lookupSession(getParent().hashCode());
            return;
        }

        for (Drawable drawable : session.getDrawables()) {
            drawable.draw(g);
        }
    }

    private void drawMouse(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.drawLine(mouseX - 7, mouseY - 7, mouseX + 7, mouseY + 7);
        g.drawLine(mouseX + 7, mouseY - 7, mouseX - 7, mouseY + 7);
        if (System.currentTimeMillis() - lastPressTime < 1000) {
            g.setColor(Color.RED);
            g.drawLine(lastClickX - 7, lastClickY - 7, lastClickX + 7,
                    lastClickY + 7);
            g.drawLine(lastClickX + 7, lastClickY - 7, lastClickX - 7,
                    lastClickY + 7);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.lastClickX = e.getX();
        this.lastClickY = e.getY();
        this.lastPressTime = System.currentTimeMillis();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
