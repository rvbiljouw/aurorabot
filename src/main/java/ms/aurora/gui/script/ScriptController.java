package ms.aurora.gui.script;

import ms.aurora.api.script.Script;
import ms.aurora.core.Session;

import static ms.aurora.gui.ApplicationController.getSelectedSession;

/**
 * @author rvbiljouw
 */
public class ScriptController {

    private ScriptController() { }

    public static void onScriptOverview() {
        ScriptOverview overview = new ScriptOverview();
        overview.setVisible(true);
    }

    public static void runScript(Script script) {
        Session session = getSelectedSession();
        if (session != null) {
            session.getScriptManager().start(script);
        }
    }

    public static void pauseScript() {
        Session session = getSelectedSession();
        if (session != null) {
            session.getScriptManager().pause();
        }
    }

    public static void resumeScript() {
        Session session = getSelectedSession();
        if (session != null) {
            session.getScriptManager().resume();
        }
    }

    public static void stopScript() {
        Session session = getSelectedSession();
        if (session != null) {
            session.getScriptManager().stop();
        }
    }

    public static void forceStopScript() {
        Session session = getSelectedSession();
        if (session != null) {
            session.getScriptManager().shutdownNow();
        }
    }

}
