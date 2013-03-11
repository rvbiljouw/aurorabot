package ms.aurora.core.input;

import java.awt.*;


/**
 * @author rvbiljouw
 */
public class InputManager {
    private final MouseProvider mouse;
    private final KeyboardProvider keyboard;

    public InputManager(MouseProvider mouse, KeyboardProvider keyboard) {
        this.mouse = mouse;
        this.keyboard = keyboard;
    }

    public synchronized MouseProvider getMouse() {
        return mouse;
    }

    public synchronized KeyboardProvider getKeyboard() {
        return keyboard;
    }

    public synchronized void setDestination(Component component) {
        mouse.setComponent(component);
        keyboard.setComponent(component);
    }
}
