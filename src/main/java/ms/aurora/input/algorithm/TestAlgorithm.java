package ms.aurora.input.algorithm;

import ms.aurora.input.VirtualMouse;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class TestAlgorithm implements VirtualMouse.MousePathAlgorithm {
    @Override
    public Point[] generatePath(Point origin, Point destination) {
        double dist = origin.distance(destination);
        double xStep = (destination.x - origin.x) / dist;
        double yStep = (destination.y - origin.y) / dist;
        Point[] path = new Point[(int) dist];
        path[0] = origin;
        path[(int) dist - 1] = destination;
        for (int i = 1; i < dist - 1; i++) {
            path[i] = new Point((int) (origin.x + (xStep * i)), (int) (origin.y + (yStep * i)));
        }
        return path;
    }
}
