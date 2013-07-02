package ms.aurora.core;

import ms.aurora.api.Context;
import ms.aurora.api.event.EventBus;
import ms.aurora.api.event.EventHandler;
import ms.aurora.api.event.RegionUpdateEvent;
import ms.aurora.api.methods.web.model.World;
import ms.aurora.api.util.BreakHandler;
import ms.aurora.core.model.Account;
import ms.aurora.core.script.PluginManager;
import ms.aurora.core.script.ScriptManager;
import ms.aurora.gui.widget.AppletWidget;
import ms.aurora.loader.ClientWrapper;
import ms.aurora.rt3.IRegion;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.model.RegionDataPacket;
import ms.aurora.sdn.net.api.repository.StatusRepository;
import ms.aurora.sdn.net.packet.RegionData;

import java.applet.Applet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ms.aurora.api.Context.getClient;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author Rick
 */
public final class Session implements Runnable {
    private final Map<Integer, List<Integer>> planeMap = new HashMap<Integer, List<Integer>>();
    private final List<RegionDataPacket> checks = new ArrayList<RegionDataPacket>();
    private final SessionProperties properties = new SessionProperties();
    private final ClientWrapper wrapper = new ClientWrapper();
    private final ThreadGroup threadGroup;
    private final EventBus eventBus;
    private final SessionUI ui;
    private ScriptManager scriptManager;
    private PluginManager pluginManager;
    private Account account;
    private long lastSyncTime = 0;
    private StatusRepository statusRepository;
    private BreakHandler breakHandler;

    public Session(ThreadGroup threadGroup, AppletWidget container) {
        this.ui = new SessionUI(this, container);
        this.eventBus = new EventBus(threadGroup);
        this.threadGroup = threadGroup;
    }

    @Override
    public void run() {
        wrapper.start();
        if (wrapper.getApplet() != null) {
            scriptManager = new ScriptManager(this);
            pluginManager = new PluginManager(this);
            statusRepository = new StatusRepository(this);
            update();
        }

        eventBus.register(this);

        while (!Thread.currentThread().isInterrupted()) {
            if (wrapper.isLoaded() && Context.isLoggedIn()) {
                List<Integer> mappedIDs = new ArrayList<Integer>();
                if(this.planeMap.containsKey(getClient().getPlane())) {
                    mappedIDs = this.planeMap.get(getClient().getPlane());
                } else {
                    this.planeMap.put(getClient().getPlane(), mappedIDs);
                }

                int rx = getClient().getBaseX();
                int ry = getClient().getBaseY();

                IRegion region = getClient().getRegions()[getClient().getPlane()];
                int[][] flagData = region.getClippingMasks();
                int[][] newArray = flagData.clone();
                for (int i = 0; i < flagData.length; i++)
                    newArray[i] = flagData[i].clone();
                for (int i = 1; i < newArray.length / 8 - 1; i++) {
                    int[] ydata = newArray[i];
                    for (int j = 1; j < ydata.length / 8 - 1; j++) {
                        int[][] subdata = new int[8][8];
                        for (int sx = 0; sx < 8; sx++)
                            for (int sy = 0; sy < 8; sy++)
                                subdata[sx][sy] = newArray[i * 8 + sx][j
                                        * 8 + sy];
                        RegionDataPacket check = new RegionDataPacket(rx + i * 8, ry + j * 8,
                                getClient().getPlane(), subdata);
                        if (!mappedIDs.contains(calcId(check.getBaseX(), check.getBaseY()))) {
                            mappedIDs.add(calcId(check.getBaseX(), check.getBaseY()));
                            checks.add(check);
                        }
                    }
                }
            }

            if ((System.currentTimeMillis() - lastSyncTime) >= 15000) {
                SDNConnection.getInstance().writePacket(new RegionData(checks));
                checks.clear();
                lastSyncTime = System.currentTimeMillis();
            }
            sleepNoException(1000);
        }
    }

    private int calcId(int x, int y) {
        return ((((x / 8) & 0xFFF) << 12) | ((y / 8) & 0xFFF));
    }


    @EventHandler
    public void onRegionUpdate(RegionUpdateEvent event) {

    }

    public void destroy() {
        try {
            scriptManager.stop();
            pluginManager.stop();
            wrapper.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SessionUI getUI() {
        return ui;
    }

    public Applet getApplet() {
        return wrapper.getApplet();
    }

    public void update() {
        ui.getContainer().setApplet(wrapper.getApplet());
        Repository.set(wrapper.getApplet().hashCode(), this);
        pluginManager.refresh();
    }

    public SessionProperties getProperties() {
        return properties;
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    public Account getAccount() {
        return account;
    }

    public BreakHandler getBreakHandler() {
        return breakHandler;
    }

    public void setAccount(Account account) {
        this.account = account;
        this.ui.update();
    }

    public void setWorld(World world) {
        if (wrapper.setWorld(this, world)) {
            ui.getContainer().setApplet(wrapper.getApplet());
        }
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public StatusRepository getStatusRepository() {
        return statusRepository;
    }
}
