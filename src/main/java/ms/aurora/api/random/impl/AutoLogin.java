package ms.aurora.api.random.impl;

import ms.aurora.api.random.Random;

import java.awt.*;

import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author tobiewarburton
 */
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
        get().input.getMouse().clickMouse((int) userRect.getCenterX(),
                (int) userRect.getCenterY(), true);
        sleepNoException(700, 1000);
        get().input.getKeyboard().type(username, true);
        sleepNoException(500, 1200);
        get().input.getKeyboard().type(password, true);
        sleepNoException(500, 1200);
        return -1;
    }
}
