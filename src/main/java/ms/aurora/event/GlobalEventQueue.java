package ms.aurora.event;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

/**
 * @author rvbiljouw
 */
public class GlobalEventQueue extends EventQueue {

    @Override
    public void dispatchEvent(AWTEvent event) {
        if (event.getSource() instanceof Canvas) {
            if (event instanceof FocusEvent || (event instanceof MouseEvent && ((MouseEvent)event).getID() == MouseEvent.MOUSE_EXITED)) {
                return;
            }
        }
        super.dispatchEvent(event);
    }

}
