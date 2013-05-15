package ms.aurora.api.script.task.impl;

import ms.aurora.api.Context;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManfiest;
import ms.aurora.api.random.impl.*;
import ms.aurora.api.script.ScriptState;
import ms.aurora.api.script.task.PassiveTask;
import ms.aurora.core.entity.EntityLoader;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.currentThread;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * Random solving task
 *
 * @author rvbiljouw
 */
public class Randoms extends PassiveTask {
    private final Logger logger = Logger.getLogger(Randoms.class);

    private final Random[] RANDOMS = {
            new AutoLogin(), new Talker(),
            new Teleother(), new WelcomeScreen(),
            new StrangeBox(), new Pinball(),
            new MrMordaut(), new FrogCave(),
            new Mime(), new Ent(),
            new Swarm()
    };

    @Override
    public boolean canRun() {
        return true; // Can always run
    }

    @Override
    public int execute() {
        List<Random> allRandoms = EntityLoader.randomEntityLoader.getEntitys();
        allRandoms.addAll(Arrays.asList(RANDOMS));
        for (Random random : RANDOMS) {
            random.setSession(Context.get().getSession());
            if (random.getClass().getAnnotation(AfterLogin.class) != null &&
                    !Context.isLoggedIn()) continue;

            try {
                while (random.activate() && !currentThread().isInterrupted()) {
                    if (Context.get().getSession().getScriptManager().getState() == ScriptState.STOP) {
                        return -1;
                    }

                    queue.getOwner().setState(ScriptState.PAUSED);
                    RandomManfiest manfiest = random.getManifest();
                    logger.info("Random  '" + manfiest.name() + " - " + manfiest.version()
                            + "' by '" + manfiest.author() + "' triggered..");
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
