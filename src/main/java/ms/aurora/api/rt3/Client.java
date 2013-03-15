package ms.aurora.api.rt3;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public interface Client {

    public Widget[][] getWidgetCache();

    public int getCameraX();

    public int getCameraY();

    public int getCameraZ();

    public int getCameraYaw();

    public int getCameraPitch();

    public String[] getMenuTargets();

    public String[] getMenuActions();

    public boolean isMenuOpen();

    public int getMenuCount();

    public int getMenuX();

    public int getMenuY();

    public int getMenuWidth();

    public int getMenuHeight();

    public Player[] getAllPlayers();

    public Player getLocalPlayer();

    public Npc[] getAllNpcs();

    public int getLoopCycle();

    public WorldController getWorld();

    public int getPlane();

    public int getBaseX();

    public int getBaseY();

    public byte[][][] getTileSettings();

    public int[][][] getTileHeights();

    public Canvas getCanvas();

    public int getMinimapInt1();

    public int getMinimapInt2();

    public int getMinimapInt3();

    public Bag getWidgetNodeBag();

    public Item[][][] getGroundItems();

    public int getLoginIndex();

    public int[] getBoundsY();

    public int[] getBoundsX();

    public int[] getBoundsHeight();

    public int[] getBoundsWidth();

    public int[] getSkillLevelBases();

    public int[] getSkillExperiences();

    public int[] getSkillLevels();

    public Mouse getMouse();

}
