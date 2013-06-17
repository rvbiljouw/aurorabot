package ms.aurora.rt3;

import java.awt.*;

/**
 * @author Rick
 */
public interface IClient {

    IWidget[][] getWidgetCache();

    int getCameraX();

    int getCameraY();

    int getCameraZ();

    int getCameraYaw();

    int getCameraPitch();

    String[] getMenuTargets();

    String[] getMenuActions();

    boolean isMenuOpen();

    int getMenuCount();

    int getMenuX();

    int getMenuY();

    int getMenuWidth();

    int getMenuHeight();

    IPlayer[] getAllPlayers();

    IPlayer getLocalPlayer();

    INpc[] getAllNpcs();

    int getLoopCycle();

    IWorldController getWorld();

    int getPlane();

    int getBaseX();

    int getBaseY();

    byte[][][] getTileSettings();

    int[][][] getTileHeights();

    Canvas getCanvas();

    int getMinimapInt1();

    int getMinimapInt2();

    int getMinimapInt3();

    IBag getWidgetNodeBag();

    IDeque[][][] getGroundItems();

    int getLoginIndex();

    int[] getBoundsY();

    int[] getBoundsX();

    int[] getBoundsHeight();

    int[] getBoundsWidth();

    int[] getSkillLevelBases();

    int[] getSkillExperiences();

    int[] getSkillLevels();

    IMouse getMouse();

    int[] getWidgetSettings();

    IRegion[] getRegions();

}
