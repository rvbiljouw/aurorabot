package ms.aurora.gui.plugin;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import ms.aurora.Messages;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;
import ms.aurora.core.Session;
import ms.aurora.core.entity.EntityLoader;
import ms.aurora.core.model.PluginConfig;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;
import static ms.aurora.core.Repository.getAll;
import static ms.aurora.core.model.PluginConfig.getByName;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 30/03/13
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public class PluginOverview extends AnchorPane {
    private static final Logger logger = Logger.getLogger(PluginOverview.class);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<PluginModel> tblPlugins;

    @FXML
    private TableColumn<PluginModel, String> colAuthor;

    @FXML
    private TableColumn<PluginModel, String> colName;

    @FXML
    private TableColumn<PluginModel, String> colShortDesc;

    @FXML
    private TableColumn<PluginModel, Boolean> colState;

    @FXML
    private TextField txtName;

    public PluginOverview() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PluginOverview.fxml"), Messages.getBundle());

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        getScene().getWindow().hide();
        refreshAllPlugins();
    }

    @FXML
    void onOk(ActionEvent event) {
        getScene().getWindow().hide();
        refreshAllPlugins();
    }

    @FXML
    void enableAction(ActionEvent event) {
        ObservableList<PluginModel> selected = tblPlugins.getSelectionModel().getSelectedItems();
        for (PluginModel select : selected) {
            if (select != null) {
                Class<? extends Plugin> plugin = select.plugin;
                PluginConfig config = getByName(plugin.getName());
                config.setEnabled(true);
                if (config.getId() == null) {
                    config.save();
                } else {
                    config.update();
                }
                select.setState(true);
            }
        }
        tblPlugins.setItems(rebuild());
    }


    @FXML
    void disableAction(ActionEvent event) {
        ObservableList<PluginModel> selected = tblPlugins.getSelectionModel().getSelectedItems();
        for (PluginModel select : selected) {
            if (select != null) {
                Class<? extends Plugin> plugin = select.plugin;
                PluginConfig config = getByName(plugin.getName());
                config.setEnabled(false);
                if (config.getId() == null) {
                    config.save();
                } else {
                    config.update();
                }
                select.setState(false);
            }
        }
        tblPlugins.setItems(rebuild());
    }

    private static void refreshAllPlugins() {
        for (Session session : getAll()) {
            // TODO:
           // session.refreshPlugins();
        }
    }

    @FXML
    void onSearch(ActionEvent event) {
        tblPlugins.setItems(rebuild());
    }

    private ObservableList<PluginModel> rebuild() {
        List<Class<? extends Plugin>> plugins = EntityLoader.get().plugins();
        ObservableList<PluginModel> pluginModelList = observableArrayList();
        String filterName = txtName.getText().toLowerCase();
        for (Class<? extends Plugin> plugin : plugins) {
            PluginManifest manifest = plugin.getAnnotation(PluginManifest.class);
            if (filterName.length() == 0 || manifest.name().toLowerCase().contains(filterName)) {
                pluginModelList.add(new PluginModel(plugin, manifest));
            }
        }
        return pluginModelList;
    }

    @FXML
    void initialize() {
        assert tblPlugins != null : "fx:id=\"tblPlugins\" was not injected: check your FXML file 'ScriptOverview.fxml'.";
        assert txtName != null : "fx:id=\"txtName\" was not injected: check your FXML file 'ScriptOverview.fxml'.";
        colName.setCellValueFactory(new PropertyValueFactory<PluginModel, String>("name"));
        colShortDesc.setCellValueFactory(new PropertyValueFactory<PluginModel, String>("shortDesc"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<PluginModel, String>("author"));
        colState.setCellValueFactory(new PropertyValueFactory<PluginModel, Boolean>("state"));

        tblPlugins.setItems(rebuild());
    }

    public static class PluginModel {
        protected final Class<? extends Plugin> plugin;
        protected final PluginManifest manifest;
        private SimpleStringProperty name;
        private SimpleStringProperty shortDesc;
        private SimpleStringProperty author;
        private SimpleBooleanProperty state;

        public PluginModel(Class<? extends Plugin> plugin, PluginManifest manifest) {
            PluginConfig config = getByName(plugin.getName());

            this.manifest = manifest;
            this.plugin = plugin;
            this.name = new SimpleStringProperty(manifest.name());
            this.shortDesc = new SimpleStringProperty(manifest.shortDescription());
            this.author = new SimpleStringProperty(manifest.author());
            this.state = new SimpleBooleanProperty(config.isEnabled());
        }

        public String getName() {
            return name.getValue();
        }

        public String getShortDesc() {
            return shortDesc.getValue();
        }

        public String getAuthor() {
            return author.getValue();
        }

        public Boolean getState() {
            return state.getValue();
        }

        public void setState(boolean state) {
            this.state.setValue(state);
        }
    }
}
