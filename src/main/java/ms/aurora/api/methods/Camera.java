package ms.aurora.api.methods;

import ms.aurora.api.ClientContext;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.VirtualKeyboard;

import java.awt.event.KeyEvent;

/**
 * Date: 27/03/13
 * Time: 17:16
 *
 * @author A_C/Cov
 */
public final class Camera {

    private final ClientContext ctx;

    public Camera(ClientContext ctx) {
        this.ctx = ctx;
    }

    private int getAngle(){
        double mapAngle = this.ctx.getClient().getCameraPitch();
        mapAngle /= 2048;
        mapAngle *= 360;
        return (int) mapAngle;
    }

    public void setCompass(char dir) {
        switch (Character.toLowerCase(dir)) {
            case 'n':
                this.setAngle(359);
                break;
            case 'e':
                this.setAngle(89);
                break;
            case 's':
                this.setAngle(179);
                break;
            case 'w':
                this.setAngle(269);
                break;
        }
    }

    public void setAngle(int degrees) {
        VirtualKeyboard keyboard = this.ctx.input.getKeyboard();
        char left = KeyEvent.VK_LEFT, right = KeyEvent.VK_RIGHT, dir = left;
        int start = this.getAngle();
        start = start < 180 ? start + 360 : start;
        degrees = degrees < 180 ? degrees+360 : degrees;
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
        while ((this.getAngle() > degrees + 5 || this.getAngle() < degrees - 5) && turnTime < 6000) {
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
