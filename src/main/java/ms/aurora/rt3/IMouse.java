package ms.aurora.rt3;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author Rick
 */
public interface IMouse extends MouseListener, MouseMotionListener {

    int getRealX();

    int getRealY();

}
