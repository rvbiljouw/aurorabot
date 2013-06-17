package ms.aurora.input.action.impl;

import ms.aurora.api.util.Utilities;
import ms.aurora.input.MouseEventChain;
import ms.aurora.input.VirtualMouse;
import ms.aurora.input.action.MouseInputAction;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Date: 17/06/13
 * Time: 12:19
 *
 * @author A_C/Cov
 */
public abstract class MouseDraggedAction extends MouseInputAction {

    @Override
    public void begin() {
        VirtualMouse.dispatchChain(MouseEventChain.createMousePressed(VirtualMouse.getMouse().getRealX(), VirtualMouse.getMouse().getRealY(), MouseEvent.BUTTON1));
    }

    @Override
    public void end() {
        VirtualMouse.dispatchChain(MouseEventChain.createMouseReleased(VirtualMouse.getMouse().getRealX(), VirtualMouse.getMouse().getRealY(), MouseEvent.BUTTON1));
    }

    @Override
    public MouseEventChain getMouseEventChain() {
        Point current = new Point(VirtualMouse.getMouse().getRealX(), VirtualMouse.getMouse().getRealY());
        Point target = getTarget();
        return MouseEventChain.createMouseDrag(ZETA_MOUSE_GENERATOR.generate(current, target), MouseEvent.BUTTON1);
    }

}
