package ms.aurora.api;

import ms.aurora.api.methods.*;
import ms.aurora.rt3.Client;
import ms.aurora.api.methods.tabs.Bank;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.core.Session;
import ms.aurora.input.InputManager;

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

    public final Bank bank;
    public final Inventory inventory;

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

        bank = new Bank(this);
        inventory = new Inventory(this);
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
