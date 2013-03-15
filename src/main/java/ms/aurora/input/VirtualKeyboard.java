package ms.aurora.input;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * @author tobiewarburton
 */
public class VirtualKeyboard {
    private Random random = new Random();
    private Component component;

    public void type(String text) {
        for (char key : text.toCharArray()) {
            press(key);
        }
    }

    public void press(char key) {
        holdKey(key, 0);
    }

    public void holdKey(char key, int duration) {
        _press(key, System.currentTimeMillis());
        _release(key, System.currentTimeMillis() + duration + random(20, 50));
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    /**
     * Presses a Key
     *
     * @param key  the char to send
     * @param time the System time in milliseconds to press the key, will be specified in the event
     */
    private void _press(char key, long time) {
        boolean shift = false;
        int code = key;
        if ((key >= 'a') && (key <= 'z')) {
            code -= 32;
        } else if ((key >= 'A') && (key <= 'Z')) {
            shift = true;
        }
        KeyEvent event;
        if ((code == KeyEvent.VK_LEFT) || (code == KeyEvent.VK_UP) || (code == KeyEvent.VK_DOWN)) {
            event = new KeyEvent(component, KeyEvent.KEY_PRESSED, time, 0, code,
                    getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
            component.dispatchEvent(event);
        } else {
            if (!shift) {
                event = new KeyEvent(component, KeyEvent.KEY_PRESSED, time, 0, code,
                        getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
                component.dispatchEvent(event);
                // Event Typed
                event = new KeyEvent(component, KeyEvent.KEY_TYPED, time + 0, 0, 0, key, 0);
                component.dispatchEvent(event);
            } else {
                // Event Pressed for shift key
                event = new KeyEvent(component, KeyEvent.KEY_PRESSED, time,
                        InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, (char) KeyEvent.VK_UNDEFINED,
                        KeyEvent.KEY_LOCATION_LEFT);
                component.dispatchEvent(event);
                // Event Pressed for char to send
                event = new KeyEvent(component, KeyEvent.KEY_PRESSED, time,
                        InputEvent.SHIFT_DOWN_MASK, code, getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
                component.dispatchEvent(event);
                // Event Typed for char to send
                event = new KeyEvent(component, KeyEvent.KEY_TYPED, time + 0,
                        InputEvent.SHIFT_DOWN_MASK, 0, key, 0);
                component.dispatchEvent(event);
            }
        }
    }

    /**
     * Releases a Key
     *
     * @param key  the char to release
     * @param time the System time in milliseconds to release the key, will be specified in the event
     */
    private void _release(char key, long time) {
        boolean shift = false;
        int code = key;
        if ((key >= 'a') && (key <= 'z')) {
            code -= 32;
        } else if ((key >= 'A') && (key <= 'Z')) {
            shift = true;
        }
        KeyEvent event;
        if ((code == KeyEvent.VK_LEFT) || (code == KeyEvent.VK_UP) || (code == KeyEvent.VK_DOWN)) {
            event = new KeyEvent(component, KeyEvent.KEY_RELEASED, time, 0, code,
                    getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
            component.dispatchEvent(event);
        } else {
            if (!shift) {
                event = new KeyEvent(component, KeyEvent.KEY_RELEASED, time, 0, code,
                        getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
                component.dispatchEvent(event);
                // Event Typed
                event = new KeyEvent(component, KeyEvent.KEY_TYPED, time, 0, 0, key, 0);
                component.dispatchEvent(event);
            } else {
                // Event Released for shift key
                event = new KeyEvent(component, KeyEvent.KEY_RELEASED, time,
                        InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, (char) KeyEvent.VK_UNDEFINED,
                        KeyEvent.KEY_LOCATION_LEFT);
                component.dispatchEvent(event);
                // Event Pressed for char to send
                event = new KeyEvent(component, KeyEvent.KEY_RELEASED, time,
                        InputEvent.SHIFT_DOWN_MASK, code, getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
                component.dispatchEvent(event);
                // Event Typed for char to send
                event = new KeyEvent(component, KeyEvent.KEY_TYPED, time,
                        InputEvent.SHIFT_DOWN_MASK, 0, key, 0);
                component.dispatchEvent(event);
            }
        }
    }

    private char getKeyCode(final char c) {
        if ((c >= 36) && (c <= 40)) {
            return KeyEvent.VK_UNDEFINED;
        } else {
            return c;
        }
    }

    private int random(int min, int max) {
        return (max - min) * random.nextInt() + min;
    }
}
