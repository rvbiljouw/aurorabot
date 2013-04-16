package ms.aurora.input;

import ms.aurora.api.Context;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
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
     * This can be called from scripts and sends a String to the applet.
     *
     * @param s The string to send.
     */
    public static void sendKeys(String s, boolean enter) {
        for (char c : s.toCharArray()) {
            clickKey(c);
        }

        if (enter) {
            clickKey('\n');
        }
    }

    /**
     * This can be called from scripts and sends a character to the applet.
     *
     * @param c The character to send.
     */
    public static void clickKey(char c) {
        for (KeyEvent ke : createKeyClick(getComponent(), c)) {
            getComponent().dispatchEvent(ke);
            sleep(getRandom());
        }
    }

    /**
     * This can be called from scripts and sends a key to the applet.
     *
     * @param keyCode The key code to send.
     */
    public static void clickKey(int keyCode) {
        for (KeyEvent ke : createKeyClick(getComponent(), keyCode)) {
            getComponent().dispatchEvent(ke);
            try {
                Thread.sleep(getRandom());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void holdKey(int keyCode) {
        KeyEvent event = getSpecialKeyEvent(getComponent(), KeyEvent.KEY_PRESSED, keyCode, System.currentTimeMillis());
        getComponent().dispatchEvent(event);
    }

    public static void releaseKey(int keyCode) {
        KeyEvent event = getSpecialKeyEvent(getComponent(), KeyEvent.KEY_RELEASED, keyCode, System.currentTimeMillis());
        getComponent().dispatchEvent(event);
    }

    /* Internal event construction  */

    private static final HashMap<Character, Character> specialChars;

    static {        //todo more dynamic: support dvorak, qwertz etc auto detection
        char[] spChars = {'~', '!', '@', '#', '%', '^', '&', '*', '(', ')', '_', '+', '{', '}', ':', '<', '>', '?', '"', '|'};
        char[] replace = {'`', '1', '2', '3', '5', '6', '7', '8', '9', '0', '-', '=', '[', ']', ';', ',', '.', '/', '\'', '\\'};
        specialChars = new HashMap<Character, Character>(spChars.length);
        for (int x = 0; x < spChars.length; ++x) {
            specialChars.put(spChars[x], replace[x]);
        }
    }

    /**
     * Gets a random number.
     *
     * @return Random number used in bewtween keystrokes and also presses.
     */
    private static long getRandom() {
        Random rand = new Random();
        return rand.nextInt(35) + 16;    //todo vary
    }

    /**
     * Generates events for pressing a key that sends a character, also takes care of the needed masks and changes
     * whatever is needed for special characters so that the events are legitimate.
     *
     * @param target Component the events are being sent to.
     * @param c      The character to send.
     * @return A KeyEvent array for you to dispatch to the component.
     */
    private static KeyEvent[] createKeyClick(Component target, char c) {
        //takes about 2x as long to get to another key than to release a key?

        Character newChar = specialChars.get(c);
        int keyCode = (int) Character.toUpperCase((newChar == null) ? c : newChar);

        if (Character.isLowerCase(c) || (!Character.isLetter(c) && (newChar == null))) {
            KeyEvent pressed = new KeyEvent(target, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, c, KeyEvent.KEY_LOCATION_STANDARD);
            KeyEvent typed = new KeyEvent(target, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, 0, c);
            KeyEvent released = new KeyEvent(target, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode, c, KeyEvent.KEY_LOCATION_STANDARD);

            return new KeyEvent[]{pressed, typed, released};
        } else {
            KeyEvent shiftDown = new KeyEvent(target, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.SHIFT_MASK, KeyEvent.VK_SHIFT, KeyEvent.CHAR_UNDEFINED);

            KeyEvent pressed = new KeyEvent(target, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.SHIFT_MASK, keyCode, c);
            KeyEvent typed = new KeyEvent(target, KeyEvent.KEY_TYPED, System.currentTimeMillis(), KeyEvent.SHIFT_MASK, 0, c);
            KeyEvent released = new KeyEvent(target, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), KeyEvent.SHIFT_MASK, keyCode, c);
            KeyEvent shiftUp = new KeyEvent(target, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_SHIFT, KeyEvent.CHAR_UNDEFINED);

            return new KeyEvent[]{shiftDown, pressed, typed, released, shiftUp};
        }
    }

    /**
     * Generates events for pressing a key that doesn't send a character, also takes care of the needed masks.
     *
     * @param target  Component the events are being sent to.
     * @param keyCode The keycode to send.
     * @return A KeyEvent array for you to dispatch to the component.
     */
    private static KeyEvent[] createKeyClick(Component target, int keyCode) {
        KeyEvent pressed = getSpecialKeyEvent(target, KeyEvent.KEY_PRESSED, keyCode, System.currentTimeMillis());
        KeyEvent released = getSpecialKeyEvent(target, KeyEvent.KEY_RELEASED, keyCode, System.currentTimeMillis() + getRandom());
        return new KeyEvent[]{pressed, released};
    }

    /**
     * generates a key event with the right modifiers
     *
     * @param target
     * @param type
     * @param keyCode
     * @param time
     * @return
     */
    private static KeyEvent getSpecialKeyEvent(Component target, int type, int keyCode, long time) {
        int modifier = 0;
        switch (keyCode) {
            case KeyEvent.VK_SHIFT:
                modifier = KeyEvent.SHIFT_MASK;
                break;
            case KeyEvent.VK_ALT:
                modifier = KeyEvent.ALT_MASK;
                break;
            case KeyEvent.VK_CONTROL:
                modifier = KeyEvent.CTRL_MASK;
                break;
        }
        return new KeyEvent(target, type, time, type == KeyEvent.KEY_PRESSED ? modifier : 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void holdControl() {
        holdKey(KeyEvent.VK_CONTROL);
    }

    public static void releaseControl() {
        releaseKey(KeyEvent.VK_CONTROL);
    }

    public static Component getComponent() {
        return Context.getClient().getCanvas();
    }
}
