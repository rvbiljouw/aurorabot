package ms.aurora.gui.world;

import javafx.beans.property.StringProperty;
import ms.aurora.browser.Browser;
import ms.aurora.browser.ContextBuilder;
import ms.aurora.browser.ResponseHandler;
import ms.aurora.browser.exception.ParsingException;
import ms.aurora.browser.impl.GetRequest;
import ms.aurora.browser.wrapper.Plaintext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

/**
 * @author rvbiljouw
 */
public class WorldModel {
    public static final List<WorldModel> WORLDS = new ArrayList<WorldModel>();

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

    public static void load() {
        WORLDS.clear();
        Browser browser = new Browser(new ContextBuilder().domain("oldschool.runescape.com").build());
        GetRequest request = new GetRequest("/slu");
        browser.doRequest(request, new ResponseHandler() {
            @Override
            public void handleResponse(InputStream inputStream) {
                try {
                    Plaintext text = Plaintext.fromStream(inputStream);
                    //  e(306,true,0,"oldschool6",274,"United States","US","Old School 6");
                    Matcher m = text.regex(".\\((.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?)\\);");
                    while(m.find()) {
                        int worldNumber = Integer.parseInt(m.group(1));
                        boolean members = parseBoolean(m.group(2));
                        String worldIdent = m.group(4).replaceAll("\"", "");
                        int players = parseInt(m.group(5));
                        String country = m.group(6).replaceAll("\"", "");
                        String worldName = m.group(8).replaceAll("\"", "");

                        WorldModel model = new WorldModel();
                        model.setName(worldName + " (" + worldNumber + ")");
                        model.setCountry(country);
                        model.setMembers(members);
                        model.setWorldNo(worldNumber);
                        model.setPlayers(players);
                        model.setIdent(worldIdent);

                        WORLDS.add(model);
                    }
                } catch (ParsingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
