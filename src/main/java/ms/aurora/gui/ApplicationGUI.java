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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ms.aurora.core.Session;
import ms.aurora.core.SessionRepository;
import ms.aurora.core.model.Account;
import ms.aurora.core.script.ScriptManager;
import ms.aurora.event.GlobalEventQueue;
import ms.aurora.gui.account.AccountOverview;
import ms.aurora.gui.plugin.PluginOverview;
import ms.aurora.gui.script.ScriptOverview;
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
    private Button btnPause;

    @FXML
    private MenuItem pluginOverview;

    @FXML
    private ToggleButton btnToggleInput;

    public ApplicationGUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ApplicationGUI.fxml"));

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
        Session session = new Session(widget);
        ThreadGroup threadGroup = new ThreadGroup(String.valueOf(tabPane.getTabs().size() + 1));
        Thread thread = new Thread(threadGroup, session);
        thread.start();
        tabPane.getTabs().add(widget.tab());
    }

    @FXML
    void onStartScript(ActionEvent evt) {
        if (getSelectedApplet() != null) {
            final Session session = SessionRepository.get(getSelectedApplet().hashCode());
            final Button source = (Button) evt.getSource();

            switch (session.getScriptManager().getState()) {
                case RUNNING:
                case PAUSED:
                    session.getScriptManager().stop();
                    source.setText("Play");
                    break;

                case STOPPED:
                    Stage stage = new Stage();
                    stage.setTitle("Select a script");
                    stage.setWidth(810);
                    stage.setHeight(640);
                    stage.initModality(Modality.WINDOW_MODAL);
                    ScriptOverview overview = new ScriptOverview();
                    Scene scene = new Scene(overview);
                    scene.getStylesheets().add("blue.css");
                    stage.setScene(scene);
                    stage.show();

                    stage.setOnHiding(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent windowEvent) {
                            if (session.getScriptManager().getState() == ScriptManager.State.RUNNING) {
                                source.setText("Stop");
                            }
                        }
                    });
                    break;


            }
        }
    }

    @FXML
    void onPauseScript(ActionEvent evt) {
        if (getSelectedApplet() != null) {
            Session session = SessionRepository.get(getSelectedApplet().hashCode());
            Button source = (Button) evt.getSource();
            switch (session.getScriptManager().getState()) {
                case RUNNING:
                    session.getScriptManager().pause();
                    source.setText("Resume");
                    break;

                case PAUSED:
                    session.getScriptManager().resume();
                    source.setText("Pause");
                    break;
            }
        }
    }

    @FXML
    void onPluginOverview(ActionEvent evt) {
        Stage stage = new Stage();
        stage.setTitle("Plugin overview");
        stage.setWidth(810);
        stage.setHeight(640);
        stage.initModality(Modality.APPLICATION_MODAL);
        PluginOverview overview = new PluginOverview();
        Scene scene = new Scene(overview);
        scene.getStylesheets().add("blue.css");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onAccounts(ActionEvent evt) {
        Stage stage = new Stage();
        stage.setTitle("Account overview");
        stage.setWidth(825);
        stage.setHeight(530);
        stage.initModality(Modality.APPLICATION_MODAL);
        AccountOverview overview = new AccountOverview();
        Scene scene = new Scene(overview);
        scene.getStylesheets().add("blue.css");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onToggleInput(ActionEvent evt) {
        ToggleButton button = (ToggleButton) evt.getSource();
        if (!button.isSelected()) {
            GlobalEventQueue.blocking = false;
            button.setText("Disable input");
            button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("enabled.png"))));
        } else {
            GlobalEventQueue.blocking = true;
            button.setText("Enable input");
            button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("disabled.png"))));
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
        btnToggleInput.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("enabled.png"))));
        Account.init();
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
                    switch (session.getScriptManager().getState()) {

                        case STOPPED:
                            self.btnPause.setText("Pause");
                            self.btnPlay.setText("Play");
                            break;

                        case RUNNING:
                            self.btnPause.setText("Pause");
                            self.btnPlay.setText("Stop");
                            break;

                        case PAUSED:
                            self.btnPause.setText("Resume");
                            self.btnPlay.setText("Stop");
                            break;

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

    public static synchronized ApplicationGUI getInstance() {
        return self;
    }
}
