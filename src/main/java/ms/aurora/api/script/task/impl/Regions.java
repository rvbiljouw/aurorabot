package ms.aurora.api.script.task.impl;

import ms.aurora.api.script.task.PassiveTask;
import ms.aurora.api.wrappers.RSPlayer;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.packet.RegionDataCheck;

import static ms.aurora.api.Context.getClient;
import static ms.aurora.api.methods.Players.getLocal;

/**
 * @author tobiewarburton
 */
public class Regions extends PassiveTask {
    private int baseX;
    private int baseY;
    private int plane;
    private int[][] masks;

    @Override
    public boolean canRun() {
        return getClient() != null && (getClient().getBaseX() != baseX
                || getClient().getBaseY() != baseY
                || getClient().getPlane() != plane);
    }

    @Override
    public int execute() {
        baseX = getClient().getBaseX();
        baseY = getClient().getBaseY();
        plane = getClient().getPlane();
        masks = getClient().getRegions()[plane].getClippingMasks();
        SDNConnection.getInstance().writePacket(new RegionDataCheck(baseX, baseY, plane, masks));
        return 10000;
    }
}
