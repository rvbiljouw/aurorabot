package ms.aurora.api.util;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 13/03/2013
 * Time: 21:36
 * To change this template use File | Settings | File Templates.
 */
public class Utilities {
    public static int random(int min, int max) {
        return (int) (min + (Math.random() * max));
    }
}