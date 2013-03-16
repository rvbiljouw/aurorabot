package ms.aurora.api.util;

/**
 * @author tobiewarburton
 */
public final class Utilities {

    private Utilities() { }

    public static int random(int min, int max) {
        return (int) (min + (Math.random() * max));
    }

    public static void sleepNoException(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}