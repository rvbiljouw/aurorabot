package ms.aurora.api.wrappers;

import ms.aurora.api.Projection;
import ms.aurora.api.rt3.Model;
import ms.aurora.api.util.GrahamScan;
import ms.aurora.api.util.Utilities;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * @author rvbiljouw
 */
public final class RSModel {
    private int[] trianglesX,
            trianglesY,
            trianglesZ,
            verticesX,
            verticesY,
            verticesZ;
    private int localX;
    private int localY;
    private int turnDirection;


    public RSModel(Model wrapped, int localX, int localY, int turnDirection) {
        this.trianglesX = wrapped.getTrianglesX().clone();
        this.trianglesY = wrapped.getTrianglesY().clone();
        this.trianglesZ = wrapped.getTrianglesZ().clone();
        this.verticesX = wrapped.getVerticesX().clone();
        this.verticesY = wrapped.getVerticesY().clone();
        this.verticesZ = wrapped.getVerticesZ().clone();
        this.localX = localX;
        this.localY = localY;
        this.turnDirection = turnDirection;
    }

    public Polygon[] getPolygons() {
        ArrayList<Polygon> polys = new ArrayList<Polygon>();
        setRotation(turnDirection);

        for (int i = 0; i < trianglesX.length; i++) {
            if (i >= trianglesY.length && i >= trianglesZ.length) return null;
            Point x = Projection.worldToScreen(new RSTile(localX, localY), verticesX[trianglesX[i]], verticesZ[trianglesX[i]], - verticesY[trianglesX[i]]);
            Point y = Projection.worldToScreen(new RSTile(localX, localY), verticesX[trianglesY[i]], verticesZ[trianglesY[i]], - verticesY[trianglesY[i]]);
            Point z = Projection.worldToScreen(new RSTile(localX, localY), verticesX[trianglesZ[i]], verticesZ[trianglesZ[i]], - verticesY[trianglesZ[i]]);
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
        for (int i = 0; i < verticesX.length; i++) {
            int i1 = verticesZ[i] * sin + verticesX[i] * cos >> 15;
            verticesZ[i] = verticesZ[i] * cos - verticesX[i] * sin >> 15;
            verticesX[i] = i1;
        }
    }

    public Polygon getHull() {
        ArrayList<Point> modelVertices = new ArrayList<Point>();
        for (int i = 0; i < trianglesX.length; i++) {
            if (i >= trianglesY.length && i >= trianglesZ.length) return null;
            Point x = Projection.worldToScreen(new RSTile(localX, localY), verticesX[trianglesX[i]], verticesZ[trianglesX[i]], - verticesY[trianglesX[i]]);
            Point y = Projection.worldToScreen(new RSTile(localX, localY), verticesX[trianglesY[i]], verticesZ[trianglesY[i]], - verticesY[trianglesY[i]]);
            Point z = Projection.worldToScreen(new RSTile(localX, localY), verticesX[trianglesZ[i]], verticesZ[trianglesZ[i]], - verticesY[trianglesZ[i]]);
            if (x.x > 0 && x.y > 0 && y.x > 0 && y.y > 0 && z.x > 0 && z.y > 0) {
                modelVertices.add(x);
                modelVertices.add(y);
                modelVertices.add(z);
            }
        }
        Polygon hull = new Polygon();
        java.util.List<Point> points = GrahamScan.getConvexHull(modelVertices);
        if (points == null) return hull;
        for (Point p: points) {
            hull.addPoint(p.x, p.y);
        }
        return hull;
    }

    public Point getRandomHullPoint() {
        Polygon hull = this.getHull();
        Rectangle bounds = hull.getBounds();
        Point p = new Point(-1, -1);
        do {
            Point temp = new Point(Utilities.random(bounds.x, bounds.x + bounds.width), Utilities.random(bounds.y, bounds.y + bounds.height));
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
