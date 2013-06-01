package ms.aurora.api.script.task.impl;

import ms.aurora.api.Context;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.random.impl.*;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptState;
import ms.aurora.api.script.task.PassiveTask;
import org.apache.log4j.Logger;

import java.util.ArrayList;
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
    private final List<Random> randoms = new ArrayList<Random>();
    private final Random[] RANDOMS = {
            new AutoLogin(), new Talker(),
            new Teleother(), new WelcomeScreen(),
            new StrangeBox(), new Pinball(),
            new FrogCave(), new Mime(),
            new Ent(), new Swarm(),
            new Unsupported()
    };

    public Randoms () {
        randoms.addAll(Arrays.asList(RANDOMS));
    }

    @Override
    public boolean canRun() {
        return Script.isActive(); // Can always run
    }

    @Override
    public int execute() {
        for (Random random : randoms) {
            if (random.getClass().getAnnotation(AfterLogin.class) != null &&
                    !Context.isLoggedIn()) continue;

            try {
                while (Script.isActive() && random.activate() && !currentThread().isInterrupted()) {
                    if (!Context.isActive()) {
                        return -1;
                    }

                    queue.getOwner().setState(ScriptState.PAUSED);
                    RandomManifest manifest = random.getManifest();
                    logger.info("Random  '" + manifest.name() + " - " + manifest.version()
                            + "' by '" + manifest.author() + "' triggered..");
                    int time = random.loop();
                    if (time == -1) break;
                    sleepNoException(time);
                }
            } catch (Exception e) {
                logger.error("Random has failed.", e);
                return -1;
            }
            queue.getOwner().setState(ScriptState.RUNNING);
        }
        return 5000;
    }
}
