package ms.aurora.api.util;

import static java.lang.Thread.currentThread;

/**
 * @author tobiewarburton
 */
public final class Utilities {

    private Utilities() {
    }

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

    public static void sleepNoException(int min, int max) {
        try {
            Thread.sleep(random(min, max));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sleeps until the passed predicate returns true.
     * @param predicate predicate
     */
    public static void sleepUntil(StatePredicate predicate) {
        while(!predicate.apply() && !currentThread().isInterrupted()) {
            sleepNoException(random(10, 20)); // Prevent it from slerping CPU.
        }
    }

    /**
     * Sleeps until the passed predicate returns true or sleeping for longer than the timeout.
     * @param predicate predicate
     * @param timeOut millis time out
     */
    public static void sleepUntil(StatePredicate predicate, long timeOut) {
        Timer timer = new Timer(timeOut);
        while(!predicate.apply() && !currentThread().isInterrupted() && !timer.finished()) {
            sleepNoException(random(10, 20)); // Prevent it from slerping CPU.
        }
    }

    /**
     * Sleeps until the passed predicate returns false.
     * @param predicate predicate
     */
    public static void sleepWhile(StatePredicate predicate) {
        while(predicate.apply() && !currentThread().isInterrupted()) {
            sleepNoException(random(10, 20)); // Prevent it from slerping CPU.
        }
    }



}