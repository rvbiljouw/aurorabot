package ms.aurora.input;

import ms.aurora.api.Context;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * A class controlling the virtual keyboard
 *
 * TODO: Implement a hook for the keyboard listener in the client
 * TODO: so we dont have potentially focus stealing code in here.
 * @author tobiewarburton
 */
public final class VirtualKeyboard {
    private static Random random = new Random();

    /**
     * Types a string of text, optionally pressing enter when the text has been typed.
     * @param text
     * @param enter
     */
    public static void type(String text, boolean enter) {
        for (char key : text.toCharArray()) {
            press(key);
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
        }
        if (enter) {
            press((char) KeyEvent.VK_ENTER);
        }
    }

    public static void press(char key) {
        _press(key, System.currentTimeMillis());
    }

    public static void holdKey(char key) {
        _press(key, System.currentTimeMillis());
    }

    public static void releaseKey(char key) {
        _release(key, System.currentTimeMillis());
    }

    /**
     * Presses a Key
     *
     * @param key  the char to send
     * @param time the System time in milliseconds to press the key, will be specified in the event
     */
    private static void _press(char key, long time) {
        boolean shift = false;
        int code = key;
        if ((key >= 'a') && (key <= 'z')) {
            code -= 32;
        } else if ((key >= 'A') && (key <= 'Z')) {
            shift = true;
        }
        KeyEvent event;
        if ((code == KeyEvent.VK_LEFT) || (code == KeyEvent.VK_UP) || (code == KeyEvent.VK_DOWN)) {
            event = new KeyEvent(getComponent(), KeyEvent.KEY_PRESSED, time, 0, code,
                    getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
            getComponent().dispatchEvent(event);
        } else {
            if (!shift) {
                event = new KeyEvent(getComponent(), KeyEvent.KEY_PRESSED, time, 0, code,
                        getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
                getComponent().dispatchEvent(event);
                // Event Typed
                event = new KeyEvent(getComponent(), KeyEvent.KEY_TYPED, time + 0, 0, 0, key, 0);
                getComponent().dispatchEvent(event);
            } else {
                // Event Pressed for shift key
                event = new KeyEvent(getComponent(), KeyEvent.KEY_PRESSED, time,
                        InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, (char) KeyEvent.VK_UNDEFINED,
                        KeyEvent.KEY_LOCATION_LEFT);
                getComponent().dispatchEvent(event);
                // Event Pressed for char to send
                event = new KeyEvent(getComponent(), KeyEvent.KEY_PRESSED, time,
                        InputEvent.SHIFT_DOWN_MASK, code, getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
                getComponent().dispatchEvent(event);
                // Event Typed for char to send
                event = new KeyEvent(getComponent(), KeyEvent.KEY_TYPED, time + 0,
                        InputEvent.SHIFT_DOWN_MASK, 0, key, 0);
                getComponent().dispatchEvent(event);
            }
        }
    }

    /**
     * Releases a Key
     *
     * @param key  the char to release
     * @param time the System time in milliseconds to release the key, will be specified in the event
     */
    private static void _release(char key, long time) {
        boolean shift = false;
        int code = key;
        if ((key >= 'a') && (key <= 'z')) {
            code -= 32;
        } else if ((key >= 'A') && (key <= 'Z')) {
            shift = true;
        }
        KeyEvent event;
        if ((code == KeyEvent.VK_LEFT) || (code == KeyEvent.VK_UP) || (code == KeyEvent.VK_DOWN)) {
            event = new KeyEvent(getComponent(), KeyEvent.KEY_RELEASED, time, 0, code,
                    getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
            getComponent().dispatchEvent(event);
        } else {
            if (!shift) {
                event = new KeyEvent(getComponent(), KeyEvent.KEY_RELEASED, time, 0, code,
                        getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
                getComponent().dispatchEvent(event);
                // Event Typed
                event = new KeyEvent(getComponent(), KeyEvent.KEY_TYPED, time, 0, 0, key, 0);
                getComponent().dispatchEvent(event);
            } else {
                // Event Released for shift key
                event = new KeyEvent(getComponent(), KeyEvent.KEY_RELEASED, time,
                        InputEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SHIFT, (char) KeyEvent.VK_UNDEFINED,
                        KeyEvent.KEY_LOCATION_LEFT);
                getComponent().dispatchEvent(event);
                // Event Pressed for char to send
                event = new KeyEvent(getComponent(), KeyEvent.KEY_RELEASED, time,
                        InputEvent.SHIFT_DOWN_MASK, code, getKeyCode(key), KeyEvent.KEY_LOCATION_STANDARD);
                getComponent().dispatchEvent(event);
                // Event Typed for char to send
                event = new KeyEvent(getComponent(), KeyEvent.KEY_TYPED, time,
                        InputEvent.SHIFT_DOWN_MASK, 0, key, 0);
                getComponent().dispatchEvent(event);
            }
        }
    }

    private static char getKeyCode(final char c) {
        if ((c >= 36) && (c <= 40)) {
            return KeyEvent.VK_UNDEFINED;
        } else {
            return c;
        }
    }

    private static int random(int min, int max) {
        return (max - min) * random.nextInt() + min;
    }

    public static Component getComponent() {
        return Context.get().getClient().getCanvas();
    }
}
