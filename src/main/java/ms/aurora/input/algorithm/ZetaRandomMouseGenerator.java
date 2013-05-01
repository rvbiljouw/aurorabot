package ms.aurora.input.algorithm;

import ms.aurora.input.MousePathGenerator;

import java.awt.*;

/**
 * Date: 01/05/13
 * Time: 16:58
 *
 * @author A_C/Cov
 */
public class ZetaRandomMouseGenerator implements MousePathGenerator {
    @Override
    public Point[] generate(Point a, Point b) {
        int steps = (int) (Math.sqrt(a.distance(b)) * 4);
        int multiplier = 1;
        double r = 3.3;
        double xOffset = (b.x - a.x) / (Math.sqrt(a.distance(b)) * 3);
        double yOffset = (b.y - a.y) / (Math.sqrt(a.distance(b)) * 3);
        double x = ((180 / ((double) steps)) * r);
        double y = ((180 / ((double) steps)) * r);
        Point[] path = new Point[steps + 2];
        if ((Math.random()) >= 0.5) {
            x *= (int) (((Math.random()) * 6)) + 1;
        }
        if ((Math.random()) >= 0.5) {
            y *= (int) (((Math.random()) * 6)) + 1;
        }
        if (Math.random() >= 0.5) {
            multiplier *= -1;
        }
        double offset = (Math.random() * (r + Math.sqrt(steps)) + 6) + 12;
        for (int i = 0; i < steps + 1; i++) {
            path[i] = new Point((a.x + ((int) (xOffset * i) + (multiplier * (int) (offset * r * Math.sin(x * i * i))))),
                    (a.y + ((int) (yOffset * i) + (multiplier * (int) (offset * r * Math.sin(y * i * i))))));
        }
        path[path.length - 1] = b;
        return path;
    }
}
