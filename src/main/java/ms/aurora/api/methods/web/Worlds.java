package ms.aurora.api.methods.web;

import ms.aurora.api.Context;
import ms.aurora.api.methods.web.model.World;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.browser.Browser;
import ms.aurora.browser.ContextBuilder;
import ms.aurora.browser.ResponseHandler;
import ms.aurora.browser.exception.ParsingException;
import ms.aurora.browser.impl.GetRequest;
import ms.aurora.browser.wrapper.Plaintext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.util.Collections.sort;
import static ms.aurora.api.util.Utilities.random;

/**
 * @author rvbiljouw
 * @author tobiewarburton
 */
public class Worlds {

    /**
     * Retrives the World with the least players
     *
     * @return the most empty world.
     */
    public static World getEmptiest() {
        List<World> worlds = getAll();
        sort(worlds, new Comparator<World>() {
            @Override
            public int compare(World o1, World o2) {
                return o1.getPlayers() - o2.getPlayers();
            }
        });
        return worlds.size() > 0 ? worlds.get(0) : null;
    }

    /**
     * Retrives the world with the most players
     *
     * @return the busiest world.
     */
    public static World getBusiest() {
        List<World> worlds = getAll();
        sort(worlds, new Comparator<World>() {
            @Override
            public int compare(World o1, World o2) {
                return o2.getPlayers() - o1.getPlayers();
            }
        });
        return worlds.size() > 0 ? worlds.get(0) : null;
    }

    /**
     * Retrives a list of all the worlds and filters them with the specified filters.
     *
     * @return a filtered list of worlds.
     */
    public static List<World> getFiltered(Predicate<World>... preds) {
        return ArrayUtils.filter(getAll().toArray(new World[0]), preds);
    }

    /**
     * Randomly selects a world from all of them.
     * 
     * @return a random world    
     */
    public static World getAny() {
        List<World> worlds = getAll();
        return (worlds.size() > 0) ? worlds.get(random(0, worlds.size() - 1)) : null;
    }

    /**
     * Switches to the specified world
     *
     * @param world the world to switch to.
     */
    public static void switchTo(World world) {
        Context.getSession().setWorld(world);
    }

    /**
     * Retrives a list of all the worlds.
     *
     * @return a list of all the worlds.
     */
    public static List<World> getAll() {
        final List<World> worlds = new ArrayList<World>();
        Browser browser = new Browser(new ContextBuilder().domain("oldschool.runescape.com").build());
        GetRequest request = new GetRequest("/slu");
        browser.doRequest(request, new ResponseHandler() {
            @Override
            public void handleResponse(InputStream inputStream) {
                try {
                    Plaintext text = Plaintext.fromStream(inputStream);
                    //  e(306,true,0,"oldschool6",274,"United States","US","Old School 6");
                    Matcher m = text.regex(".\\((.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?)\\);");
                    while (m.find()) {
                        int worldNumber = Integer.parseInt(m.group(1));
                        boolean members = parseBoolean(m.group(2));
                        String worldIdent = m.group(4).replaceAll("\"", "");
                        int players = parseInt(m.group(5));
                        String country = m.group(6).replaceAll("\"", "");
                        String worldName = m.group(8).replaceAll("\"", "");

                        World model = new World();
                        model.setName(worldName + " (" + worldNumber + ")");
                        model.setCountry(country);
                        model.setMembers(members);
                        model.setWorldNo(worldNumber);
                        model.setPlayers(players);
                        model.setIdent(worldIdent);

                        worlds.add(model);
                    }
                } catch (ParsingException e) {
                    e.printStackTrace();
                }
            }
        });
        return worlds;
    }

}
