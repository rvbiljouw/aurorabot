package ms.aurora.core.script;

import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptState;
import ms.aurora.core.Session;
import ms.aurora.core.script.exception.NoScriptRunningException;
import ms.aurora.core.script.exception.ScriptAlreadyRunningException;
import ms.aurora.gui.ApplicationGUI;

public final class ScriptManager {
    private final Session session;
    private Script currentScript;
    private Thread currentThread;

    public ScriptManager(Session session) {
        this.session = session;
    }

    public void start(Script script) {
        if (!hasScript()) {
            currentThread = new Thread(session.getThreadGroup(), script);
            currentScript = script;
            currentScript.setSession(session);
            currentScript.setState(ScriptState.START);
            currentThread.start();

            ApplicationGUI.setInputEnabled(false);
            ApplicationGUI.update();
        } else {
            throw new ScriptAlreadyRunningException(currentScript);
        }
    }

    public void pause() {
        if (hasScript()) {
            currentScript.setState(ScriptState.PAUSED);

            ApplicationGUI.setInputEnabled(true);
            ApplicationGUI.update();
        } else {
            throw new NoScriptRunningException();
        }
    }

    public void resume() {
        if (hasScript()) {
            currentScript.setState(ScriptState.RUNNING);

            ApplicationGUI.setInputEnabled(false);
            ApplicationGUI.update();
        } else {
            throw new NoScriptRunningException();
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

        ApplicationGUI.setInputEnabled(true);
        ApplicationGUI.update();
    }

    private boolean hasScript() {
        return currentScript != null && currentThread != null;
    }

    public ScriptState getState() {
        return currentScript != null ? currentScript.getState() : ScriptState.STOP;
    }
}
