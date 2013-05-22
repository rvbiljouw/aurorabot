package ms.aurora.gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

import java.applet.Applet;
import java.io.IOException;

import static java.lang.String.valueOf;


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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ApplicationGUI.fxml"),
                Messages.getBundle());
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            self = this;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
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
        assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'Application.fxml'.";
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
        String ident = valueOf(tabPane.getTabs().size() + 1);
        ThreadGroup threadGroup = new ThreadGroup(ident);
        Session session = new Session(threadGroup, widget);
        Thread thread = new Thread(threadGroup, session);
        thread.start();
        tabPane.getTabs().add(widget.getTab());
    }

    public void onStart() {
        if (getSelectedApplet() != null) {
            EntityLoader.reload();
            Dialog dialog = new ScriptOverview();
            dialog.show();
        }
    }

    public void onStop() {
        if (getSelectedApplet() != null) {
            Session session = Repository.get(getSelectedApplet().hashCode());
            session.getScriptManager().stop();
        }
    }

    public void onPause() {
        if (getSelectedApplet() != null) {
            Session session = Repository.get(getSelectedApplet().hashCode());
            session.getScriptManager().pause();
        }
    }

    public void onResume() {
        if (getSelectedApplet() != null) {
            Session session = Repository.get(getSelectedApplet().hashCode());
            session.getScriptManager().resume();
        }
    }

    public void onPlugins() {
        EntityLoader.reload();
        Dialog dialog = new PluginOverview();
        dialog.show();
    }

    public void onAccounts() {
        Dialog dialog = new AccountOverview();
        dialog.show();
    }

    public void onSettings() {
        Dialog dialog = new Properties();
        dialog.show();
    }

    public void onAbout() {

    }

    public void onForums() {

    }

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

    public void onInput() {
        if (btnInput.isSelected()) {
            GlobalEventQueue.blocking = true;
            btnInput.setGraphic(Icons.INPUT_DISABLED);
        } else {
            GlobalEventQueue.blocking = false;
            btnInput.setGraphic(Icons.INPUT_ENABLED);
        }
    }

    public void onHide() {
        MessageBox.show(getScene().getWindow(), "Not supported",
                "Not supported", MessageBox.OK);
    }

    public void onClose() {
        System.exit(0);
    }

}
