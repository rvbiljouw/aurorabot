import ms.aurora.api.methods.Minimap;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;
import ms.aurora.core.model.Grid;
import ms.aurora.core.model.GridNode;
import ms.aurora.event.listeners.PaintListener;
import ms.aurora.rt3.Region;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author rvbiljouw
 */
@PluginManifest(name = "World Logger", version = 0.0)
public class WorldLogger extends Plugin implements PaintListener {
    private final Timer timer = new Timer();


    @Override
    public void startup() {
        timer.scheduleAtFixedRate(new TimerTask() {
            int lastBaseX;
            int lastBaseY;

            @Override
            public void run() {
                try {
                    if (lastBaseX != getClient().getBaseX() || lastBaseY != getClient().getBaseY()) {
                        Region region = getClient().getRegions()[getClient().getPlane()];
                        lastBaseX = getClient().getBaseX();
                        lastBaseY = getClient().getBaseY();
                        Grid grid = Grid.getByBase(lastBaseX, lastBaseY);
                        if (grid == null) {
                            grid = new Grid();
                            grid.setBaseX(lastBaseX);
                            grid.setBaseY(lastBaseY);
                            grid.save();

                            for (int i = 0; i < 104; i++) {
                                for (int j = 0; j < 104; j++) {
                                    int mask = region.getClippingMasks()[i][j];
                                    GridNode node = new GridNode();
                                    node.setGrid(grid);
                                    node.setLocalX(i);
                                    node.setLocalY(j);
                                    node.setMask(mask);
                                    node.save();
                                }
                            }
                        } else {
                            info(String.format("Grid %d,%d already logged.", lastBaseX, lastBaseX));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000L, 2000L);
    }

    @Override
    public void execute() {
    }

    @Override
    public void cleanup() {
        timer.cancel();
    }


    @Override
    public void onRepaint(Graphics graphics) {
        Grid grid = Grid.getByBase(getClient().getBaseX(), getClient().getBaseY());
        if (grid != null) {
            for (GridNode node : grid.getGridNodes()) {
                Point minimapPoint = Minimap.convert(getClient().getBaseX() + node.getLocalX(),
                        getClient().getBaseY() + node.getLocalY());
                if (minimapPoint.x != -1 && minimapPoint.y != -1) {
                    if (solid(node.getMask())) {
                        graphics.setColor(Color.RED);
                    } else {
                        graphics.setColor(Color.GREEN);
                    }
                    graphics.drawRect(minimapPoint.x, minimapPoint.y, 1, 1);
                }
            }
        }
    }

    public boolean solid(int mask) {
        return blocked(mask, DIRECTION_NORTH) ||
                blocked(mask, DIRECTION_SOUTH) ||
                blocked(mask, DIRECTION_EAST) ||
                blocked(mask, DIRECTION_WEST) ||
                blocked(mask, DIRECTION_NORTHEAST) ||
                blocked(mask, DIRECTION_NORTHWEST) ||
                blocked(mask, DIRECTION_SOUTHEAST) ||
                blocked(mask, DIRECTION_SOUTHWEST);
    }

    public boolean blocked(int mask, int direction) {
        return (mask & direction) != 0;
    }

    private static final int DIRECTION_NORTH = 0x1280120;
    private static final int DIRECTION_SOUTH = 0x1280102;
    private static final int DIRECTION_EAST = 0x1280180;
    private static final int DIRECTION_WEST = 0x1280108;

    private static final int DIRECTION_NORTHEAST = 0x12801e0;
    private static final int DIRECTION_NORTHWEST = 0x1280138;
    private static final int DIRECTION_SOUTHEAST = 0x1280183;
    private static final int DIRECTION_SOUTHWEST = 0x128010e;
}
