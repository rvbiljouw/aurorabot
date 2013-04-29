package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.Locatable;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.input.VirtualKeyboard;

import javax.rmi.CORBA.Util;
import java.awt.event.KeyEvent;

import static ms.aurora.api.util.Utilities.sleepNoException;

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
        double mapAngle = Context.getClient().getCameraYaw();
        mapAngle /= 2048;
        mapAngle *= 360;
        return (int) mapAngle;
    }

    /**
     * Retrieves the current camera pitch
     *
     * @return camera pitch
     */
    public static int getPitch() {
        return (int) ((Context.getClient().getCameraPitch() - 1024) / 20.48);
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
        int left = KeyEvent.VK_LEFT, right = KeyEvent.VK_RIGHT, dir = left;
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
        VirtualKeyboard.holdKey(dir);
        int timeWaited = 0;
        int turnTime = 0;
        while ((getAngle() > degrees + 5 || getAngle() < degrees - 5) && turnTime < 6000) {
            sleepNoException(10);
            turnTime += 10;
            timeWaited += 10;
            if (timeWaited > 500) {
                int time = timeWaited - 500;
                if (time == 0 || time % 40 == 0) {
                    VirtualKeyboard.holdKey(dir);
                }
            }
        }
        VirtualKeyboard.releaseKey(dir);
    }

    /**
     * Moves the camera view to the extreme up or down.
     *
     * @param up true move camera up, false down.
     */
    public static void setPitch(boolean up) {
        int direction = up ? KeyEvent.VK_UP : KeyEvent.VK_DOWN;
        VirtualKeyboard.holdKey(direction);
        sleepNoException(1200);
        VirtualKeyboard.releaseKey(direction);
    }

    /**
     * Sets the camera to the specified value.
     *
     * @param pitch pitch of the height.
     */
    public static void setPitch(int pitch) {
        int direction = getPitch() > pitch ? KeyEvent.VK_DOWN : KeyEvent.VK_UP;
        VirtualKeyboard.holdKey(direction);
        int timeWaited = 0;
        int turnTime = 0;
        while ((getPitch() > pitch + 5 || getAngle() < pitch - 5) && turnTime < 6000) {
            sleepNoException(10);
            turnTime += 10;
            timeWaited += 10;
            if (timeWaited > 500) {
                int time = timeWaited - 500;
                if (time == 0 || time % 40 == 0) {
                    VirtualKeyboard.holdKey(direction);
                }
            }
        }
        VirtualKeyboard.releaseKey(direction);
    }

    private static int getAngleTo(RSTile tile) {
        RSTile playerLocation = Players.getLocal().getLocation();
        int angle = ((int) Math.toDegrees(Math.atan2(tile.getY() - playerLocation.getY(),
                tile.getX() - playerLocation.getX()))) - 90;
        if (angle < 0) {
            angle += 360;
        }
        return angle % 360;
    }

    /**
     * Turns to the tile with the given deviation.
     *
     * @param tile RSTile to turn camera to.
     * @param deviation degree of accuracy.
     */
    public static void turnTo(RSTile tile, int deviation) {
        int angle = getAngleTo(tile);
        angle = Utilities.random(angle - deviation, angle + deviation + 1);
        setAngle(angle);
    }

    /**
     * Turns to the tile.
     *
     * @param tile RSTile to turn to.
     */
    public static void turnTo(RSTile tile) {
        turnTo(tile, 0);
    }

    /**
     * Turns to the given Locatable with the given deviation.
     *
     * @param locatable Locatable to turn to.
     * @param deviation degree of accuracy.
     */
    public static void turnTo(Locatable locatable, int deviation) {
        turnTo(locatable.getLocation(), deviation);
    }

    /**
     * Turns to the given Locatable.
     * @param locatable Locatable to turn to.
     */
    public static void turnTo(Locatable locatable) {
        turnTo(locatable.getLocation(), 0);
    }

}
