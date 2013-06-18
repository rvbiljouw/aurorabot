package ms.aurora.input.action;

import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.Interactable;
import ms.aurora.input.MouseEventChain;
import ms.aurora.input.VirtualMouse;
import ms.aurora.input.algorithm.MousePathGenerator;
import ms.aurora.input.algorithm.impl.StraightLineGenerator;
import ms.aurora.input.algorithm.impl.ZetaMouseGenerator;

import java.awt.*;

/**
 * Date: 17/06/13
 * Time: 11:56
 *
 * @author A_C/Cov
 */
public abstract class MouseInputAction {


    protected static final MousePathGenerator ZETA_MOUSE_GENERATOR = new ZetaMouseGenerator();
    protected static final MousePathGenerator STRAIGHT_MOUSE_GENERATOR = new StraightLineGenerator();

    public abstract Point getTarget();
    public abstract void begin();
    public abstract void end();
    public abstract MouseEventChain getMouseEventChain();
    public abstract boolean canStep();

    public final void step(int index, MouseEventChain chain) {
        VirtualMouse.dispatchEvent(chain.getMouseEvents()[index]);
        Utilities.sleepNoException(chain.getSleepTimes()[index]);
    }

    public final void apply() {
        begin();
        int index = 0;
        MouseEventChain chain = getMouseEventChain();
        for (int i = 0; i < 10 && index != chain.getMouseEvents().length; i++, chain = getMouseEventChain(), index = 0) {
            while (canStep() && index != chain.getMouseEvents().length) {
                step(index++, chain);
            }
        }
        end();
    }

}
