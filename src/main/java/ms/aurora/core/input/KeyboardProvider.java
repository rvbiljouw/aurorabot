package ms.aurora.core.input;

import java.awt.*;


/**
 * @author rvbiljouw
 */
public interface KeyboardProvider {

    /**
     * Types a string of text
     *
     * @param text Text to type
     */
    public void type(String text);

    /**
     * Simulates a key press
     *
     * @param key The key to simulate
     */
    public void press(char key);

    /**
     * Simulates a key press and hold for a specific duration
     *
     * @param key
     */
    public void holdKey(char key, int duration);

    /**
     * Holds the key until a condition is met.
     *
     * @param predicate a predicate.
     */
    public void holdKeyUntil(char key, KeyboardPredicate predicate);


    /**
     * Set the target component for all further events.
     *
     * @param component
     */
    public void setComponent(Component component);
}
