package ms.aurora.api.methods.web.model;

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
public class World {
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
