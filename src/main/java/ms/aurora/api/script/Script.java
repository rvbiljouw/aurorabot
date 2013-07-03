package ms.aurora.api.script;

import ms.aurora.api.Context;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.impl.*;
import ms.aurora.api.util.Timer;
import ms.aurora.core.script.EntityLoader;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rick
 */
public abstract class Script extends Context implements Runnable {
    private final List<Random> randoms = new ArrayList<Random>();
    private final Logger logger = Logger.getLogger(getClass());
    private ScriptState state = ScriptState.START;
    private Timer runTimer;

    public Script() {
    }

    public final synchronized ScriptState getState() {
        return this.state;
    }

    public final synchronized void setState(ScriptState state) {
        switch (state) {
            case PAUSED:
                onPause();
                break;
            case RUNNING:
                if (this.state == ScriptState.PAUSED) {
                    onResume();
                }
                break;
            default:
                break;
        }
        this.state = state;
    }

    public final ScriptManifest getManifest() {
        return getClass().getAnnotation(ScriptManifest.class);
    }

    @Override
    public final void run() {
        logger.info("Started " + getManifest().name() + " by " + getManifest().author());
        runTimer = new Timer();
        init();
        try {
            onStart();
            state = ScriptState.RUNNING;
            while (getState() != ScriptState.STOP) {
                String _delay = getProperty("script.delay");
                int delay = _delay == null ? 500 : Integer.parseInt(_delay);

                if (isInRandom()) {
                    int loopCycle = executeRandoms();
                    if (loopCycle < -1) break;
                    delay += loopCycle;
                    logger.info("Random cycle ended.");
                } else if (isLoggedIn() && getState() != ScriptState.PAUSED) {
                    int loopCycle = tick();
                    if (loopCycle < 0) {
                        break;
                    }
                    delay += loopCycle;
                }

                Thread.sleep(delay);
            }
        } catch (Exception e) {
            logger.error("Script error!", e);
            e.printStackTrace();
        } finally {
            onFinish();
            cleanup();
        }
    }

    public long getRunningTime() {
        return runTimer.elapsed();
    }

    private void initializeRandoms() {
        for (Class<? extends Random> randomClass : EntityLoader.getRandoms()) {
            try {
                Random random = randomClass.newInstance();
                randoms.add(random);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        randoms.add(new AutoLogin());
        randoms.add(new WelcomeScreen());

        randoms.add(new Maze());
        randoms.add(new Certer());
        randoms.add(new Ent());
        randoms.add(new FrogCave());
        randoms.add(new Mime());
        randoms.add(new Pinball());
        randoms.add(new StrangeBox());
        randoms.add(new Swarm());
        randoms.add(new Talker());
        randoms.add(new Teleother());
        randoms.add(new Unsupported());
        logger.info("Loaded " + randoms.size() + " randoms.");
    }

    private int executeRandoms() throws InterruptedException {
        for (Random random : randoms) {
            if (random.getClass().getAnnotation(AfterLogin.class) != null
                    && !isLoggedIn()) continue;


            if (random.activate() && getState() != ScriptState.STOP) {
                logger.info("Activating random " + random.getManifest().name());
                return random.loop();
            }
        }
        return 0;
    }

    public boolean isInRandom() {
        for (Random random : randoms) {
            if (random.getClass().getAnnotation(AfterLogin.class) != null
                    && !isLoggedIn()) continue;

            if (random.activate()) return true;
        }
        return false;
    }

    private void init() {
        Context.getEventBus().register(this);
        initializeRandoms();
    }

    private void cleanup() {
        Context.getEventBus().deregister(this);
        getSession().getScriptManager().stop();
    }


    public void onPause() {

    }

    public void onResume() {

    }

    public void onStart() {

    }

    public void onFinish() {

    }

    public abstract int tick();

    @Override
    public String toString() {
        return getState() + " : " + getManifest().name() + " By " + getManifest().author() + "Random ? " +
                (isInRandom() ? "Yes" : "No");
    }

}
