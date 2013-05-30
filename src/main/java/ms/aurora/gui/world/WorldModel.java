package ms.aurora.gui.world;

import javafx.beans.property.StringProperty;

/**
 * @author rvbiljouw
 */
public class WorldModel {
    private String name;
    private String country;
    private int worldNo;
    private int players;
    private boolean members;
    private String ident;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getWorldNo() {
        return worldNo;
    }

    public void setWorldNo(int worldNo) {
        this.worldNo = worldNo;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public boolean isMembers() {
        return members;
    }

    public void setMembers(boolean members) {
        this.members = members;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }
}
