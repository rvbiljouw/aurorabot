package ms.aurora.input;

import ms.aurora.api.Context;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * A class controlling the virtual keyboard
 * <p/>
 * TODO: Implement a hook for the keyboard listener in the client
 * TODO: so we dont have potentially focus stealing code in here.
 *
 * @author tobiewarburton
 */
public final class VirtualKeyboard {

    /**
     * Types a string of text, optionally pressing enter when the text has been typed.
     *
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
        KeyEvent pressed = new KeyEvent(getComponent(), KeyEvent.KEY_PRESSED, time,
                0, (int) key, key, KeyEvent.KEY_LOCATION_STANDARD);
        getComponent().dispatchEvent(pressed);
    }

    /**
     * Releases a Key
     *
     * @param key  the char to release
     * @param time the System time in milliseconds to release the key, will be specified in the event
     */
    private static void _release(char key, long time) {
        KeyEvent pressed = new KeyEvent(getComponent(), KeyEvent.KEY_RELEASED, time,
                0, (int) key, key, KeyEvent.KEY_LOCATION_STANDARD);
        getComponent().dispatchEvent(pressed);
    }

    public static void holdControl() {
        holdKey((char) KeyEvent.VK_CONTROL);
    }

    public static void releaseControl() {
        releaseKey((char) KeyEvent.VK_CONTROL);
    }

    public static Component getComponent() {
        return Context.getClient().getCanvas();
    }
}
