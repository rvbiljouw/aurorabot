package ms.aurora.api.script.task.impl;

import ms.aurora.api.Context;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.impl.*;
import ms.aurora.api.script.ScriptState;
import ms.aurora.api.script.task.PassiveTask;
import org.apache.log4j.Logger;

import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * Random solving task
 *
 * @author rvbiljouw
 */
public class Randoms extends PassiveTask {
    private final Logger logger = Logger.getLogger(Randoms.class);
    private final Random[] RANDOMS = {
            new AutoLogin(), new AxeHandler(),
            new BeehiveSolver(), new CapnArnav(),
            new ScapeRuneIsland(), new Talker(),
            new Teleother(), new WelcomeScreen(),
            new StrangeBox(), new Pinball(),
            new MrMordaut(), new FrogCave(),
    };

    @Override
    public boolean canRun() {
        return true; // Can always run
    }

    @Override
    public int execute() {
        for (Random random : RANDOMS) {
            random.setSession(Context.get().getSession());
            try {
                while (random.activate() && !Thread.currentThread().isInterrupted()) {
                    if (Context.get().getSession().getScriptManager().getState() == ScriptState.STOP) {
                        return -1;
                    }

                    queue.getOwner().setState(ScriptState.PAUSED);
                    String name = random.getClass().getSimpleName();
                    logger.info("Random  '" + name + "' triggered..");
                    int time = random.loop();
                    if (time == -1) break;
                    sleepNoException(time);
                }
            } catch (Exception e) {
                logger.error("Random has failed.", e);
                return -1;
            }
            // rvbiljouw: Make sure the state always gets set back to running..
            queue.getOwner().setState(ScriptState.RUNNING);
        }
        return 5000;
    }
}
