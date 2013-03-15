package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Widget {

    public String getText();

    public String[] getActions();

    public Widget[] getChildren();

    public Widget getDisabledComponent();

    public int getRotationX();

    public int getRotationY();

    public int getRotationZ();

    public int getOffsetX();

    public int getOffsetY();

    public int getModelZoom();

    public int getScrollMaxHorizontal();

    public int getBorderThickness();

    public int getItemId();

    public int getComponentIndex();

    public int getTextureId();

    public int getY();

    public int getItemStackSize();

    public int getX();

    public int getTextColor();

    public int getShadowColor();

    public int getParentId();

    public int getFontId();

    public int getHeight();

    public int getWidth();

    public int[][] getOpcodes();

    public String getSelectedAction();

    public int getScrollMaxVertical();

    public int getUid();

    public int getType();

    public int[] getInventoryItems();

    public int[] getInventoryStackSizes();
}
