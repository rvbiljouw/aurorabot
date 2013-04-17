package ms.aurora.api.script.task.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Calculations;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.impl.*;
import ms.aurora.api.script.ScriptState;
import ms.aurora.api.script.task.PassiveTask;
import ms.aurora.api.script.task.TaskQueue;
import org.jboss.logging.Logger;

import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * Random solving task
 *
 * @author rvbiljouw
 */
public class Randoms extends PassiveTask {
    private final Logger logger = Logger.getLogger(Randoms.class);
    private static final Random[] RANDOMS = {
            new AutoLogin(), new AxeHandler(),
            new BeehiveSolver(), new CapnArnav(),
            new ScapeRuneIsland(), new Talker(),
            new Teleother(), new WelcomeScreen(),
            new StrangeBox(), new Pinball(),
            new MrMordaut()
    };

    public Randoms(TaskQueue queue) {
        super(queue);
    }

    @Override
    public boolean canRun() {
        return Context.isLoggedIn();
    }

    @Override
    public int execute() {
        logger.info("Checking for randoms...");
        for (Random random : RANDOMS) {
            random.setSession(Context.get().getSession());
            try {
                while (random.activate()) {
                    queue.getOwner().setState(ScriptState.PAUSED);
                    String name = random.getClass().getSimpleName();
                    logger.info("Random  '" + name + "' triggered.");

                    int time = random.loop();
                    if (time == -1) continue;
                    sleepNoException(time);

                    queue.getOwner().setState(ScriptState.RUNNING);
                }
            } catch (Exception e) {
                logger.error("Random has failed.", e);
            }
        }
        logger.info("Done..");
        return 100;
    }
}
