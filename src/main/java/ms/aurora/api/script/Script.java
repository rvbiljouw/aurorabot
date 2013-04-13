package ms.aurora.api.script;

import ms.aurora.api.Context;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.impl.*;
import ms.aurora.event.listeners.PaintListener;
import org.apache.log4j.Logger;

import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author Rick
 */
public abstract class Script extends Context implements Runnable {
    private final Logger logger = Logger.getLogger(getClass());
    private ScriptState state = ScriptState.START;
    private Thread randomsThread;

    public Script() {
    }

    public abstract int tick();

    public final void info(String message) {
        logger.info(message);
    }

    public final void debug(String message) {
        logger.debug(message);
    }

    public final void error(String message) {
        logger.error(message);
    }

    public final void error(String message, Throwable t) {
        logger.error(message, t);
    }

    public final synchronized void setState(ScriptState state) {
        this.state = state;
        info("New state " + state.name());
    }

    public final synchronized ScriptState getState() {
        return this.state;
    }

    @Override
    public final void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                switch (getState()) {
                    case START:
                        init();
                        onStart();
                        setState(ScriptState.RUNNING);
                        break;


                    case RUNNING:
                        if (Context.isLoggedIn()) {
                            int loopResult = tick();
                            if (loopResult != -1) {
                                Thread.sleep(loopResult + 600);
                            } else {
                                // Returning -1 means exit.
                                info("Exited by -1");
                                return;
                            }
                        } else {
                            info("Not logged in.");
                        }
                        break;

                    case PAUSED:
                        Thread.sleep(1000);
                        break;

                    case STOP:
                        cleanup();
                        onFinish();
                        return;

                }
            } catch (InterruptedException e) {
                state = ScriptState.STOP;
                cleanup();
                onFinish();
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                state = ScriptState.STOP;
                cleanup();
                onFinish();
                logger.error("Script has thrown exception and has exited.", e);
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    public void onStart() {

    }

    public void onFinish() {

    }

    private void init() {
        if (this instanceof PaintListener) {
            getSession().getPaintManager().register((PaintListener) this);
        }
        randomsThread = new Thread(new Randoms());
        randomsThread.start();
    }

    private void cleanup() {
        if (this instanceof PaintListener) {
            getSession().getPaintManager().deregister((PaintListener) this);
        }
    }

    public final boolean validate() {
        return getManifest() != null;
    }

    public final ScriptManifest getManifest() {
        return getClass().getAnnotation(ScriptManifest.class);
    }

    private class Randoms implements Runnable {

        @Override
        public void run() {
            while (getState() != ScriptState.STOP) {
                for (Random random : randoms) {
                    random.setSession(getSession());
                    while (random.activate()) {
                        get().getSession().getScriptManager().pause();
                        info("Random event " + random.getClass().getSimpleName() + " was triggered.");
                        int time = random.loop();
                        if (time == -1) continue;
                        sleepNoException(time);
                        get().getSession().getScriptManager().resume();
                    }
                }
                sleepNoException(100);
            }
        }

    }

    private final Random[] randoms = {
            new AutoLogin(),
            new AxeHandler(),
            new BeehiveSolver(),
            new CapnArnav(),
            new ScapeRuneIsland(),
            new Talker(),
            new Teleother(),
            new WelcomeScreen(),
            new StrangeBox()
    };
}
