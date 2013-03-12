package ms.aurora.core.input;

import ms.aurora.api.ClientContext;
import ms.aurora.core.input.impl.BezierMouseProvider;
import ms.aurora.core.input.impl.StandardKeyboardProvider;

import java.applet.Applet;


/**
 * @author rvbiljouw
 */
public class InputManager {
    private final ClientContext context;
    private final MouseProvider mouse;
    private final KeyboardProvider keyboard;

    public InputManager(ClientContext context) {
        this.context = context;
        this.mouse = new BezierMouseProvider();
        this.keyboard = new StandardKeyboardProvider();
    }

    public synchronized MouseProvider getMouse() {
        mouse.setComponent(((Applet) context.getClient()).getComponentAt(0, 0));
        return mouse;
    }

    public synchronized KeyboardProvider getKeyboard() {
        mouse.setComponent(((Applet) context.getClient()).getComponentAt(0, 0));
        return keyboard;
    }
}
