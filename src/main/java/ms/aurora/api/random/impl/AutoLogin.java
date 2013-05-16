package ms.aurora.api.random.impl;

import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.input.VirtualKeyboard;

import java.awt.*;

import static ms.aurora.api.util.Utilities.sleepNoException;
import static ms.aurora.input.VirtualMouse.clickMouse;

/**
 * @author tobiewarburton
 */
@RandomManifest(name = "Auto Login", version = 1.0)
public class AutoLogin extends Random {
    private Rectangle userRect = new Rectangle(398, 278, 129, 12);

    @Override
    public boolean activate() {
        return getClient().getLoginIndex() == 10 && getSession().getAccount() != null;
    }

    @Override
    public int loop() {
        String username = getSession().getAccount().getUsername();
        String password = getSession().getAccount().getPassword();
        clickMouse((int) userRect.getCenterX(),
                (int) userRect.getCenterY(), true);
        sleepNoException(700, 1000);
        VirtualKeyboard.sendKeys(username, true);
        sleepNoException(500, 1200);
        VirtualKeyboard.sendKeys(password, true);
        sleepNoException(500, 1200);
        return 3000;
    }
}
