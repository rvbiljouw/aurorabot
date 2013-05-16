package ms.aurora.gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ms.aurora.Messages;
import ms.aurora.core.Session;
import ms.aurora.core.SessionRepository;
import ms.aurora.core.model.Property;
import ms.aurora.core.script.PauseEvent;
import ms.aurora.core.script.StopEvent;
import ms.aurora.event.GlobalEventQueue;
import ms.aurora.gui.account.AccountOverview;
import ms.aurora.gui.config.Properties;
import ms.aurora.gui.plugin.PluginOverview;
import ms.aurora.gui.script.ScriptOverview;
import ms.aurora.gui.util.FXUtils;
import ms.aurora.gui.widget.AppletWidget;

import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ApplicationGUI extends AnchorPane {
    private static ApplicationGUI self;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane tabPane;

    @FXML
    private volatile Menu mnPlugins;

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnPause;

    @FXML
    private MenuItem pluginOverview;

    @FXML
    private ToggleButton btnToggleInput;

    public ApplicationGUI() {
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


    @FXML
    void onCloseClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onNewSession(ActionEvent event) {
        AppletWidget widget = new AppletWidget(this);
        ThreadGroup threadGroup = new ThreadGroup(String.valueOf(tabPane.getTabs().size() + 1));
        Session session = new Session(threadGroup, widget); // TODO
        Thread thread = new Thread(threadGroup, session);
        thread.start();
        tabPane.getTabs().add(widget.getTab());
    }

    @FXML
    void onPauseScript(ActionEvent evt) {
        if (getSelectedApplet() != null) {
            Session session = SessionRepository.get(getSelectedApplet().hashCode());
            session.getScriptSupervisor().tell(new PauseEvent(null));
        }
        update();
    }

    @FXML
    void onPluginOverview(ActionEvent evt) {
        FXUtils.showModalDialog(Messages.getString("pluginOverview.title"), new PluginOverview());
    }

    @FXML
    void onAccounts(ActionEvent evt) {
        FXUtils.showModalDialog(Messages.getString("accountOverview.title"), new AccountOverview());
    }

    @FXML
    void onProperties(ActionEvent evt) {
        Properties properties = new Properties();
        properties.open();
    }

    @FXML
    void onStartScript(ActionEvent evt) {
        if (getSelectedApplet() != null) {
            final Session session = SessionRepository.get(getSelectedApplet().hashCode());
            if (session.getActive()) {
                session.getScriptSupervisor().tell(new StopEvent(null));
            } else {
                FXUtils.showModalDialog(Messages.getString("scriptOverview.title"), new ScriptOverview());
            }
        }
    }

    @FXML
    void onToggleInput(ActionEvent evt) {
        ToggleButton button = btnToggleInput;
        if (!button.isSelected()) {
            GlobalEventQueue.blocking = false;
            button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/enabled.png"))));
        } else {
            GlobalEventQueue.blocking = true;
            button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/disabled.png"))));
        }
    }

    @FXML
    void onUnlinkAccount(ActionEvent evt) {
        Property property = Property.getByName("forumuser");
        if (property != null) {
            Property password = Property.getByName(property.getValue());
            if (password != null) {
                password.remove();
            }
            property.remove();
        }
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
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab tab2) {
                update();
            }
        });
        btnToggleInput.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/enabled.png"))));
        btnPause.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/pause.png"))));
        btnPlay.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/play.png"))));
        btnNew.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/add.png"))));
    }

    public static Applet getSelectedApplet() {
        Tab tab = self.tabPane.getSelectionModel().getSelectedItem();
        if (tab != null && tab.getContent() instanceof AppletWidget) {
            return ((AppletWidget) tab.getContent()).getApplet();
        }
        return null;
    }

    public static void update() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Session session = getSelectedSession();
                if (session != null) {
                    if (!session.getActive()) {
                        self.btnPause.setText(Messages.getString("mainWindow.script.pause"));
                        self.btnPause.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/pause.png"))));
                        self.btnPlay.setText(Messages.getString("mainWindow.script.start"));
                        self.btnPlay.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/play.png"))));
                    } else {
                        self.btnPause.setText(Messages.getString("mainWindow.script.pause"));
                        self.btnPause.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/pause.png"))));
                        self.btnPlay.setText(Messages.getString("mainWindow.script.stop"));
                        self.btnPlay.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("icons/stop.png"))));
                    }
                }
            }
        });
    }

    public static Session getSelectedSession() {
        Tab tab = self.tabPane.getSelectionModel().getSelectedItem();
        if (tab != null && tab.getContent() instanceof AppletWidget) {
            if (((AppletWidget) tab.getContent()).getApplet() != null)
                return SessionRepository.get(((AppletWidget) tab.getContent()).getApplet().hashCode());
        }
        return null;
    }

    public synchronized Menu getPluginsMenu() {
        return mnPlugins;
    }

    public MenuItem getPluginOverview() {
        return pluginOverview;
    }

    public static void setInput(boolean enabled) {
        self.btnToggleInput.setSelected(!enabled);
        self.onToggleInput(null);
    }

    public static synchronized ApplicationGUI getInstance() {
        return self;
    }
}
