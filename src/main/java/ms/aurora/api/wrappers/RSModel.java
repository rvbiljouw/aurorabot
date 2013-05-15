package ms.aurora.api.wrappers;

import ms.aurora.api.methods.Viewport;
import ms.aurora.rt3.Model;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import static ms.aurora.api.util.Utilities.random;

/**
 * @author Rick
 */
public final class RSModel {
    private int[] trianglesX,
            trianglesY,
            trianglesZ,
            verticesX,
            verticesY,
            verticesZ,
            originalX,
            originalZ;
    private int localX;
    private int localY;
    private int orientation;


    public RSModel(Model wrapped, int localX, int localY, int orientation) {
        this.trianglesX = wrapped.getTrianglesX();
        this.trianglesY = wrapped.getTrianglesY();
        this.trianglesZ = wrapped.getTrianglesZ();
        this.verticesX = wrapped.getVerticesX().clone();
        this.verticesY = wrapped.getVerticesY().clone();
        this.verticesZ = wrapped.getVerticesZ().clone();
        this.localX = localX;
        this.localY = localY;
        this.orientation = orientation;
        this.originalX = this.verticesX.clone();
        this.originalZ = this.verticesZ.clone();

        setRotation((64 * 128) & 0x3fff);
        if (orientation != 0) {
            setRotation(orientation & 0x3fff);
        }
    }

    public Polygon[] getPolygons() {
        setRotation((64 * 128) & 0x3fff);
        if (orientation != 0) {
            setRotation(orientation & 0x3fff);
        }

        ArrayList<Polygon> polys = new ArrayList<Polygon>();
        for (int i = 0; i < trianglesX.length; i++) {
            if (i >= trianglesY.length && i >= trianglesZ.length) return null;
            Point x = Viewport.convert(new RSTile(localX, localY), verticesX[trianglesX[i]], verticesZ[trianglesX[i]], -verticesY[trianglesX[i]]);
            Point y = Viewport.convert(new RSTile(localX, localY), verticesX[trianglesY[i]], verticesZ[trianglesY[i]], -verticesY[trianglesY[i]]);
            Point z = Viewport.convert(new RSTile(localX, localY), verticesX[trianglesZ[i]], verticesZ[trianglesZ[i]], -verticesY[trianglesZ[i]]);
            if (x.x > 0 && x.y > 0 && y.x > 0 && y.y > 0 && z.x > 0 && z.y > 0) {
                int xx[] = {
                        x.x, y.x, z.x
                };
                int yy[] = {
                        x.y, y.y, z.y
                };
                polys.add(new Polygon(xx, yy, 3));
            }
        }
        return polys.toArray(new Polygon[polys.size()]);
    }

    protected void setRotation(int orientation) {
        int sin = SIN_TABLE[orientation];
        int cos = COS_TABLE[orientation];
        for (int i = 0; i < this.originalX.length; ++i) {
            this.verticesX[i] = this.originalX[i] * cos + this.originalZ[i] * sin >> 15;
            this.verticesZ[i] = this.originalZ[i] * cos - this.originalX[i] * sin >> 15;
        }

    }

    public Point getRandomPoint() {
        Polygon[] polys = getPolygons();
        if (polys != null && polys.length > 0) {
            Polygon random = polys[random(0, polys.length - 1)];
            PathIterator iter = random.getPathIterator(null);
            if (!iter.isDone()) {
                iter.next();
                double[] coords = new double[2];
                iter.currentSegment(coords);
                return new Point((int) coords[0], (int) coords[1]);
            }
        }
        return new Point(-1, -1);
    }

    public void cleanup() {
        trianglesX = trianglesY = trianglesZ = null;
        verticesX = verticesY = verticesZ = null;
        originalX = null;
        originalZ = null;
    }

    private static final int[] SIN_TABLE = new int[16384];
    private static final int[] COS_TABLE = new int[16384];

    static {
        final double d = 0.00038349519697141029D;
        for (int i = 0; i < 16384; i++) {
            SIN_TABLE[i] = (int) (32768D * Math.sin(i * d));
            COS_TABLE[i] = (int) (32768D * Math.cos(i * d));
        }
    }

}
