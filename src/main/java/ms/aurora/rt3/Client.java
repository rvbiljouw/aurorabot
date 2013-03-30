package ms.aurora.rt3;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public interface Client {

    Widget[][] getWidgetCache();

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

    Player[] getAllPlayers();

    Player getLocalPlayer();

    Npc[] getAllNpcs();

    int getLoopCycle();

    WorldController getWorld();

    int getPlane();

    int getBaseX();

    int getBaseY();

    byte[][][] getTileSettings();

    int[][][] getTileHeights();

    Canvas getCanvas();

    int getMinimapInt1();

    int getMinimapInt2();

    int getMinimapInt3();

    Bag getWidgetNodeBag();

    Deque[][][] getGroundItems();

    int getLoginIndex();

    int[] getBoundsY();

    int[] getBoundsX();

    int[] getBoundsHeight();

    int[] getBoundsWidth();

    int[] getSkillLevelBases();

    int[] getSkillExperiences();

    int[] getSkillLevels();

    Mouse getMouse();

    int[] getWidgetSettings();

}
