package ms.aurora.api.util;

/**
 * @author tobiewarburton
 */
public final class Utilities {

    private Utilities() { }

    public static int random(int min, int max) {
        return (int) (min + (Math.random() * max));
    }
}