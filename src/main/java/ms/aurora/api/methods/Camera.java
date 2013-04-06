package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.VirtualKeyboard;

import java.awt.event.KeyEvent;

/**
 * @author A_C/Cov
 * @author Rick
 */
public final class Camera {

    /**
     * Retrieves the current camera angle.
     *
     * @return camera angle
     */
    public static int getAngle() {
        double mapAngle = Context.get().getClient().getCameraPitch();
        mapAngle /= 2048;
        mapAngle *= 360;
        return (int) mapAngle;
    }

    /**
     * Sets the compass to specified face direction
     *
     * @param dir Direction
     *            'n' for North,
     *            'e' for East,
     *            's' for South,
     *            'w' for West
     */
    public static void setCompass(char dir) {
        switch (Character.toLowerCase(dir)) {
            case 'n':
                setAngle(359);
                break;
            case 'e':
                setAngle(89);
                break;
            case 's':
                setAngle(179);
                break;
            case 'w':
                setAngle(269);
                break;
        }
    }

    /**
     * Sets the camera to the specified angle
     *
     * @param degrees degrees of the turn
     */
    public static void setAngle(int degrees) {
        VirtualKeyboard keyboard = Context.get().input.getKeyboard();
        char left = KeyEvent.VK_LEFT, right = KeyEvent.VK_RIGHT, dir = left;
        int start = getAngle();
        start = start < 180 ? start + 360 : start;
        degrees = degrees < 180 ? degrees + 360 : degrees;
        if (degrees > start) {
            if (degrees - 180 < start) {
                dir = right;
            }
        } else if (start > degrees) {
            if (start - 180 >= degrees) {
                dir = right;
            }
        }
        degrees %= 360;
        keyboard.holdKey(dir);
        int timeWaited = 0;
        int turnTime = 0;
        while ((getAngle() > degrees + 5 || getAngle() < degrees - 5) && turnTime < 6000) {
            Utilities.sleepNoException(10);
            turnTime += 10;
            timeWaited += 10;
            if (timeWaited > 500) {
                int time = timeWaited - 500;
                if (time == 0 || time % 40 == 0) {
                    keyboard.holdKey(dir);
                }
            }
        }
        keyboard.releaseKey(dir);
    }

}
