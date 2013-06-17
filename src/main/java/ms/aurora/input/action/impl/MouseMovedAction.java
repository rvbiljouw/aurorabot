package ms.aurora.input.action.impl;

import ms.aurora.input.MouseEventChain;
import ms.aurora.input.VirtualMouse;
import ms.aurora.input.action.MouseInputAction;

import java.awt.*;

/**
 * Date: 17/06/13
 * Time: 12:45
 *
 * @author A_C/Cov
 */
public abstract class MouseMovedAction extends MouseInputAction {

    @Override
    public void begin() {}

    @Override
    public void end() {}

    @Override
    public MouseEventChain getMouseEventChain() {
        Point current = new Point(VirtualMouse.getMouse().getRealX(), VirtualMouse.getMouse().getRealY());
        Point target = getTarget();

        if (target.distance(current) < 20) {
            return MouseEventChain.createMousePath(STRAIGHT_MOUSE_GENERATOR.generate(current, target));
        }
        return MouseEventChain.createMousePath(ZETA_MOUSE_GENERATOR.generate(current, target));
    }

}
