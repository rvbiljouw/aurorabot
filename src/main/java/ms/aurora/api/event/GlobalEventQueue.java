package ms.aurora.api.event;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author Rick
 */
public class GlobalEventQueue extends EventQueue {
    public static boolean blocking = false;

    @Override
    public final void dispatchEvent(AWTEvent event) {
        if (event.getSource() instanceof Canvas) {
            if (event instanceof MouseEvent && blocking) {
                return;
            }
        }
        super.dispatchEvent(event);
    }

}
