package ms.aurora.input;

import ms.aurora.api.Context;

import java.applet.Applet;
import java.awt.*;


/**
 * @author Rick
 */
public class InputManager {
    private final Context context;
    private final VirtualMouse mouse;
    private final VirtualKeyboard keyboard;

    public InputManager(Context context) {
        this.context = context;
        this.mouse = new VirtualMouse();
        this.keyboard = new VirtualKeyboard();
    }

    public final synchronized VirtualMouse getMouse() {
        mouse.setComponent(this.getComponent());
        return mouse;
    }

    public final synchronized VirtualKeyboard getKeyboard() {
        this.keyboard.setComponent(this.getComponent());
        return this.keyboard;
    }

    private final synchronized Component getComponent() {
        return ((Applet) context.getClient()).getComponentAt(0, 0);
    }

    public void initialize() {

    }
}
