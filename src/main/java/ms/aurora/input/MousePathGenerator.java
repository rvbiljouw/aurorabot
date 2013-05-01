package ms.aurora.input;

import java.awt.*;

/**
 * Date: 01/05/13
 * Time: 16:25
 *
 * @author A_C/Cov
 */
public interface MousePathGenerator {
    Point[] generate(Point origin, Point destination);
}
