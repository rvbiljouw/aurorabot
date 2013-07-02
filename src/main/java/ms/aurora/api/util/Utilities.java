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

    public static double random(double min, double max) {
        return (min + (Math.random() * max));
    }


    public static long random(long min, long max) {
        return (min + ((long) Math.random() * max));
    }

    public static void sleepNoException(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep was interrupted");
        }
    }

    public static void sleepNoException(int min, int max) {
        int ms = random(min, max);
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep was interrupted");
        }
    }

    /**
     * Sleeps until the passed predicate returns true.
     *
     * @param predicate predicate
     */
    @Deprecated
    public static void sleepUntil(StatePredicate predicate) {
        while (!predicate.apply() && !currentThread().isInterrupted()) {
            try {
                Thread.sleep(random(10, 20));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep was interrupted");
            }
        }
    }

    /**
     * Sleeps until the passed predicate returns true or sleeping for longer than the timeout.
     *
     * @param predicate predicate
     * @param timeOut   millis time out
     */
    public static boolean sleepUntil(StatePredicate predicate, long timeOut) {
        Timer timer = new Timer(timeOut);
        boolean success = !predicate.apply();
        while (success && !currentThread().isInterrupted() && !timer.finished()) {
            try {
                Thread.sleep(random(10, 20));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep was interrupted");
            }
            success = !predicate.apply();
        }
        return !success;
    }

    /**
     * Sleeps until the passed predicate returns false.
     *
     * @param predicate predicate
     */
    @Deprecated
    public static void sleepWhile(StatePredicate predicate) {
        while (predicate.apply() && !currentThread().isInterrupted()) {
            try {
                Thread.sleep(random(10, 20));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep was interrupted");
            }
        }
    }

    /**
     * Sleeps until the passed predicate returns false.
     *
     * @param predicate predicate
     * @param timeOut   millis time out
     */
    public static boolean sleepWhile(StatePredicate predicate, long timeOut) {
        Timer timer = new Timer(timeOut);
        boolean success = predicate.apply();
        while (success && !currentThread().isInterrupted() && !timer.finished()) {
            try {
                Thread.sleep(random(10, 20));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep was interrupted");
            }
            success = predicate.apply();
        }
        return !success;
    }

}