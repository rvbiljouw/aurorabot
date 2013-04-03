package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Viewport;
import ms.aurora.api.util.GrahamScan;
import ms.aurora.api.util.Utilities;
import ms.aurora.rt3.Model;

import java.awt.*;
import java.util.ArrayList;

import static ms.aurora.api.util.Utilities.random;

/**
 * @author Rick
 */
public final class RSModel {
    private final Context ctx;

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


    public RSModel(Context ctx, Model wrapped, int localX, int localY, int orientation) {
        this.ctx = ctx;
        this.trianglesX = wrapped.getTrianglesX().clone();
        this.trianglesY = wrapped.getTrianglesY().clone();
        this.trianglesZ = wrapped.getTrianglesZ().clone();
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

    public Polygon getHull() {
        ArrayList<Point> modelVertices = new ArrayList<Point>();
        for (int i = 0; i < trianglesX.length; i++) {
            if (i >= trianglesY.length && i >= trianglesZ.length) return null;
            Point x = Viewport.convert(new RSTile(localX, localY), verticesX[trianglesX[i]], verticesZ[trianglesX[i]], -verticesY[trianglesX[i]]);
            Point y = Viewport.convert(new RSTile(localX, localY), verticesX[trianglesY[i]], verticesZ[trianglesY[i]], -verticesY[trianglesY[i]]);
            Point z = Viewport.convert(new RSTile(localX, localY), verticesX[trianglesZ[i]], verticesZ[trianglesZ[i]], -verticesY[trianglesZ[i]]);
            if (x.x > 0 && x.y > 0 && y.x > 0 && y.y > 0 && z.x > 0 && z.y > 0) {
                modelVertices.add(x);
                modelVertices.add(y);
                modelVertices.add(z);
            }
        }
        Polygon hull = new Polygon();
        java.util.List<Point> points = GrahamScan.getConvexHull(modelVertices);
        if (points == null) return hull;
        for (Point p : points) {
            hull.addPoint(p.x, p.y);
        }
        return hull;
    }

    /**
     * wot lolol
     *
     * @return
     */
    public Point getRandomPoint() {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < verticesX.length; i++) {
            if (i >= verticesY.length && i >= verticesZ.length) return null;
            Point point = Viewport.convert(new RSTile(localX, localY), verticesX[i], verticesZ[i], -verticesY[i]);
            if (point.x != -1 && point.y != -1) {
                points.add(point);
            }
        }
        if (points.size() != 0)
            return points.get(Utilities.random(0, points.size()));
        else return new Point(-1, -1);
    }

    public Point getRandomHullPoint() {
        Polygon hull = this.getHull();
        Rectangle bounds = hull.getBounds();
        Point p = new Point(-1, -1);
        do {
            Point temp = new Point(random(bounds.x, bounds.x + bounds.width), random(bounds.y, bounds.y + bounds.height));
            if (hull.contains(temp)) {
                p = temp;
            }
        } while (p.x == -1 || p.y == -1);
        return p;
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
