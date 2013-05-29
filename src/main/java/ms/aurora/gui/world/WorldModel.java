package ms.aurora.gui.world;

import javafx.beans.property.StringProperty;

/**
 * @author rvbiljouw
 */
public class WorldModel {
    private StringProperty name;
    private StringProperty players;
    private StringProperty country;
    private StringProperty members;

    public String getName() {
        return name.getValue();
    }

    public void setName(StringProperty name) {
        this.name = name;
    }

    public String getPlayers() {
        return players.getValue();
    }

    public void setPlayers(StringProperty players) {
        this.players = players;
    }

    public String getCountry() {
        return country.getValue();
    }

    public void setCountry(StringProperty country) {
        this.country = country;
    }

    public String getMembers() {
        return members.getValue();
    }

    public void setMembers(StringProperty members) {
        this.members = members;
    }
}
