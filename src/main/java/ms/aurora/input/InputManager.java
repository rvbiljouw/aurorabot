package ms.aurora.input;

import ms.aurora.api.ClientContext;

import java.applet.Applet;


/**
 * @author rvbiljouw
 */
public class InputManager {
    private final ClientContext context;
    private final VirtualMouse mouse;
    private final VirtualKeyboard keyboard;

    public InputManager(ClientContext context) {
        this.context = context;
        this.mouse = new VirtualMouse();
        this.keyboard = new VirtualKeyboard();
    }

    public final synchronized VirtualMouse getMouse() {
        mouse.setComponent(((Applet) context.getClient())
                .getComponentAt(0, 0));
        return mouse;
    }

    public void initialize() {

    }
}
