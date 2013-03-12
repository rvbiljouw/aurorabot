package ms.aurora.core;

import com.google.common.collect.Lists;
import ms.aurora.api.drawing.Drawable;
import ms.aurora.core.script.ScriptManager;

import java.applet.Applet;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author rvbiljouw
 */
public class Session implements Runnable {
    private final List<Drawable> drawables = Lists.newArrayList();
    private final ScriptManager scriptManager = new ScriptManager(this);
    private final Applet applet;

    public Session(Applet applet) {
        this.applet = applet;
    }

    @Override
    public void run() {
        componentSessionMap.put(getApplet().hashCode(), this);
    }

    public Applet getApplet() {
        return applet;
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    public List<Drawable> getDrawables() {
        return drawables;
    }

    public void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }

    public static synchronized Session lookupSession(Integer key) {
        return componentSessionMap.get(key);
    }

    private static final Map<Integer, Session> componentSessionMap = newHashMap();

}
