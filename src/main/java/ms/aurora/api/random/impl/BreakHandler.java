package ms.aurora.api.random.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.tabs.Logout;
import ms.aurora.api.random.Random;
import ms.aurora.api.util.Utilities;

/**
 * @author iJava
 */
public class BreakHandler extends Random {

    ms.aurora.api.util.BreakHandler.Break brea;

    @Override
    public boolean activate() {
        long runTime = Context.getSession().getScriptManager().getCurrentScript().getRunningTime();
        brea = Context.getSession().getBreakHandler().findBreak(runTime);
        return brea != null;
    }

    @Override
    public int loop() {
        //basic
        if(isLoggedIn()) {
            Logout.logout();
        }
        Utilities.sleepNoException((int) brea.getBreakTime());
        return 150;
    }
}
