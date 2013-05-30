package ms.aurora.gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import jfx.messagebox.MessageBox;
import ms.aurora.core.Repository;
import ms.aurora.core.Session;
import ms.aurora.core.model.Property;
import ms.aurora.core.script.EntityLoader;
import ms.aurora.event.GlobalEventQueue;
import ms.aurora.gui.account.AccountOverview;
import ms.aurora.gui.config.Properties;
import ms.aurora.gui.plugin.PluginOverview;
import ms.aurora.gui.script.ScriptOverview;
import ms.aurora.gui.widget.AppletWidget;
import ms.aurora.gui.world.WorldSelectDialog;

import java.applet.Applet;
import java.awt.*;
import java.net.URL;

import static java.lang.String.valueOf;
import static ms.aurora.api.javafx.FXUtils.load;


public class Main extends AnchorPane {
    private static Main self;
    @FXML
    private TabPane tabPane;
    @FXML
    private Menu mnPlugins;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnResume;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnNew;
    @FXML
    private MenuItem pluginOverview;
    @FXML
    private ToggleButton btnInput;

    public Main() {
        load(getClass().getResource("ApplicationGUI.fxml"), this);
        self = this;
    }

    /**
     * Retrieves the applet that is currently selected in the tabPane.
     * May return null
     *
     * @return selected applet or null
     */
    public static Applet getSelectedApplet() {
        Tab tab = self.tabPane.getSelectionModel().getSelectedItem();
        if (tab != null && tab.getContent() instanceof AppletWidget) {
            return ((AppletWidget) tab.getContent()).getApplet();
        }
        return null;
    }

    /**
     * Sets the global input enable flag and updates the button.
     *
     * @param enabled input allowed?
     */
    public static void setInputEnabled(boolean enabled) {
        self.btnInput.setSelected(!enabled);
        self.onInput();
    }

    public static synchronized Main getInstance() {
        return self;
    }

    @FXML
    void initialize() {
        mnPlugins.setOnShowing(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                if (selectedTab == null) {
                    return;
                }

                Node component = selectedTab.getContent();
                if (component instanceof AppletWidget) {
                    ((AppletWidget) component).onMenuOpening();
                } else {
                    mnPlugins.getItems().clear();
                    mnPlugins.getItems().add(pluginOverview);
                }
            }
        });

        btnInput.setGraphic(Icons.INPUT_ENABLED);
        btnPause.setGraphic(Icons.PAUSE);
        btnStart.setGraphic(Icons.PLAY);
        btnResume.setGraphic(Icons.RESUME);
        btnStop.setGraphic(Icons.STOP);
        btnNew.setGraphic(Icons.ADD);
    }

    public synchronized Menu getPluginsMenu() {
        return mnPlugins;
    }

    public MenuItem getPluginOverview() {
        return pluginOverview;
    }

    public void onNewSession() {
        AppletWidget widget = new AppletWidget(this);
        tabPane.getTabs().add(widget.getTab());
        tabPane.getSelectionModel().select(widget.getTab());

        String ident = valueOf(tabPane.getTabs().size() + 1);
        ThreadGroup threadGroup = new ThreadGroup(ident);
        Session session = new Session(threadGroup, widget);

        WorldSelectDialog dialog = new WorldSelectDialog();
        dialog.showAndWait();
        session.setWorld(dialog.getSelected());

        Thread thread = new Thread(threadGroup, session);
        thread.start();
    }

    /**
     * Shows the script overview if there is none running and an applet is selected.
     */
    public void onStart() {
        if (getSelectedApplet() != null) {
            EntityLoader.reload();
            ms.aurora.api.javafx.Dialog dialog = new ScriptOverview();
            dialog.show();
        }
    }

    /**
     * Stops a script if there is one running and an applet is selected.
     */
    public void onStop() {
        if (getSelectedApplet() != null) {
            Session session = Repository.get(getSelectedApplet().hashCode());
            session.getScriptManager().stop();
        }
    }

    /**
     * Pauses a script if there is one running and an applet is selected.
     */
    public void onPause() {
        if (getSelectedApplet() != null) {
            Session session = Repository.get(getSelectedApplet().hashCode());
            session.getScriptManager().pause();
        }
    }

    /**
     * Resumes a script if there is one running and an applet is selected.
     */
    public void onResume() {
        if (getSelectedApplet() != null) {
            Session session = Repository.get(getSelectedApplet().hashCode());
            session.getScriptManager().resume();
        }
    }

    /**
     * Opens the plugin overview
     */
    public void onPlugins() {
        EntityLoader.reload();
        ms.aurora.api.javafx.Dialog dialog = new PluginOverview();
        dialog.show();
    }

    /**
     * Opens the account overview
     */
    public void onAccounts() {
        ms.aurora.api.javafx.Dialog dialog = new AccountOverview();
        dialog.show();
    }

    /**
     * Opens the settings / properties dialog
     */
    public void onSettings() {
        ms.aurora.api.javafx.Dialog dialog = new Properties();
        dialog.show();
    }

    /**
     * TODO: Should open the about dialog, which isn't made yet.
     */
    public void onAbout() {

    }

    /**
     * Called when the forums menu item gets clicked
     */
    public void onForums() {
        try {
            Desktop.getDesktop().browse(new URL("http://www.aurora.ms/community").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called by the unlink menu item.
     * Removes the stored forum account, effectively
     * disabling auto login.
     */
    public void onUnlink() {
        Property property = Property.getByName("forumuser");
        if (property != null) {
            Property password = Property.getByName(property.getValue());
            if (password != null) {
                password.remove();
            }
            property.remove();

            MessageBox.show(getScene().getWindow(),
                    "Next time you open the client," +
                            " you will have to enter your" +
                            " account details again.",
                    "Success!", MessageBox.OK);
        }
    }

    /**
     * Called by the input toggle button.
     * Sets the global blocking var depending on selection state.
     * <p/>
     * The blocking will be activated for all applet widgets.
     */
    public void onInput() {
        if (btnInput.isSelected()) {
            GlobalEventQueue.blocking = true;
            btnInput.setGraphic(Icons.INPUT_DISABLED);
        } else {
            GlobalEventQueue.blocking = false;
            btnInput.setGraphic(Icons.INPUT_ENABLED);
        }
    }

    /**
     * Called by the Hide menu item
     * TODO: Go to tray
     */
    public void onHide() {
        MessageBox.show(getScene().getWindow(), "Not supported",
                "Not supported", MessageBox.OK);
    }

    /**
     * Called by the Close menu item.
     * TODO: Ask for confirmation
     */
    public void onClose() {
        System.exit(0);
    }

}
