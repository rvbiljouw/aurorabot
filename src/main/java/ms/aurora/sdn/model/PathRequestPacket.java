package ms.aurora.sdn.model;

import flexjson.JSONDeserializer;

/**
 * @author tobiewarburton
 */
public class PathRequestPacket {
    private int currentPlane;

    private int currX;
    private int currY;

    private int destX;
    private int destY;

    public PathRequestPacket(int currentPlane, int currX, int currY, int destX, int destY) {
        this.currentPlane = currentPlane;
        this.currX = currX;
        this.currY = currY;
        this.destX = destX;
        this.destY = destY;
    }

    public PathRequestPacket() {

    }

    public int getCurrentPlane() {
        return currentPlane;
    }

    public void setCurrentPlane(int currentPlane) {
        this.currentPlane = currentPlane;
    }

    public int getCurrX() {
        return currX;
    }

    public void setCurrX(int currX) {
        this.currX = currX;
    }

    public int getCurrY() {
        return currY;
    }

    public void setCurrY(int currY) {
        this.currY = currY;
    }

    public int getDestX() {
        return destX;
    }

    public void setDestX(int destX) {
        this.destX = destX;
    }

    public int getDestY() {
        return destY;
    }

    public void setDestY(int destY) {
        this.destY = destY;
    }

    private static JSONDeserializer<PathRequestPacket> deserializer = new JSONDeserializer<PathRequestPacket>();

    public static PathRequestPacket deserialize(String json) {
        return deserializer.deserialize(json);
    }
}
