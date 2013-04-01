package ms.aurora.rt3;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author rvbiljouw
 */
public interface Mouse extends MouseListener, MouseMotionListener {

    int getMouseX();

    int getMouseY();

    int getMousePressX();

    int getMousePressY();

}
