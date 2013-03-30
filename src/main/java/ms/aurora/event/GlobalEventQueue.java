package ms.aurora.event;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author rvbiljouw
 */
public class GlobalEventQueue extends EventQueue {
    public static volatile boolean blocking = false;

    @Override
    public final void dispatchEvent(AWTEvent event) {
        if (event.getSource() instanceof Canvas) {
            if(event instanceof MouseEvent && blocking) {
                return;
            }
        }
        super.dispatchEvent(event);
    }

}
