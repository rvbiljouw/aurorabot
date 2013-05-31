package ms.aurora.core.script;

import javafx.application.Platform;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptState;
import ms.aurora.core.Session;
import ms.aurora.gui.Main;

public final class ScriptManager {
    private final Session session;
    private Script currentScript;
    private Thread currentThread;

    public ScriptManager(Session session) {
        this.session = session;
    }

    public void start(Class<? extends Script> scriptClass) {
        if (!hasScript()) {
            try {
                Script script = scriptClass.newInstance();
                currentThread = new Thread(session.getThreadGroup(), script);
                currentScript = script;
                currentScript.setState(ScriptState.START);
                currentThread.start();

                Main.setInputEnabled(false);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        if (currentScript != null) {
            currentScript.setState(ScriptState.PAUSED);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Main.setInputEnabled(true);
                }
            });
        }
    }

    public void resume() {
        if (currentScript != null) {
            currentScript.setState(ScriptState.RUNNING);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Main.setInputEnabled(false);
                }
            });
        }
    }

    public void stop() {
        try {
            currentScript.setState(ScriptState.STOP);
            currentThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentThread = null;
        currentScript = null;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Main.setInputEnabled(true);
            }
        });
    }

    private boolean hasScript() {
        return currentScript != null && currentThread != null;
    }

    public ScriptState getState() {
        return currentScript != null ? currentScript.getState() : ScriptState.STOP;
    }
}
