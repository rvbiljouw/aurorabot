package ms.aurora.api;

import ms.aurora.api.methods.*;
import ms.aurora.api.methods.tabs.*;
import ms.aurora.core.Session;
import ms.aurora.input.InputManager;
import ms.aurora.rt3.Client;

/**
 * @author rvbiljouw
 */
public class ClientContext {
    public final InputManager input;
    public final Players players;
    public final Npcs npcs;
    public final GroundItems items;
    public final Objects objects;
    public final Calculations calculations;
    public final Widgets widgets;
    public final Menu menu;
    public final Walking walking;
    public final Skills skills;
    public final Camera camera;

    public final Bank bank;
    public final Inventory inventory;
    public final Tabs tabs;
    public final Magic magic;
    public final Options options;

    public final Settings settings;

    private Session session;

    public ClientContext() {
        input = new InputManager(this);
        players = new Players(this);
        npcs = new Npcs(this);
        items = new GroundItems(this);
        objects = new Objects(this);
        calculations = new Calculations(this);
        widgets = new Widgets(this);
        menu = new Menu(this);
        walking = new Walking(this);
        skills = new Skills(this);
        camera = new Camera(this);

        bank = new Bank(this);
        inventory = new Inventory(this);
        tabs = new Tabs(this);
        magic = new Magic(this);
        options = new Options(this);
        settings = new Settings(this);
    }

    public final void setSession(Session session) {
        this.session = session;
    }

    public final Session getSession() {
        return session;
    }

    public final Client getClient() {
        return (Client) session.getApplet();
    }
}
