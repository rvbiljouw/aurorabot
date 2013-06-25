package ms.aurora.gui.plugin;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;
import ms.aurora.core.impl.PluginRefreshVisitor;
import ms.aurora.core.model.PluginConfig;
import ms.aurora.core.script.EntityLoader;
import ms.aurora.api.javafx.Dialog;
import ms.aurora.gui.Messages;
import ms.aurora.api.javafx.FXUtils;

import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;
import static ms.aurora.core.Repository.foreach;
import static ms.aurora.core.model.PluginConfig.getByName;

/**
 * @author tobiewarburton
 * @author rvbiljouw
 */
public class PluginOverview extends Dialog {
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
        FXUtils.load(getClass().getResource("PluginOverview.fxml"), this);
    }

    @FXML
    void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<PluginModel, String>("name"));
        colShortDesc.setCellValueFactory(new PropertyValueFactory<PluginModel, String>("shortDesc"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<PluginModel, String>("author"));
        colState.setCellValueFactory(new PropertyValueFactory<PluginModel, Boolean>("state"));

        tblPlugins.setItems(rebuild());
    }

    @FXML
    void onCancel(ActionEvent event) {
        foreach(new PluginRefreshVisitor());
        close();
    }

    @FXML
    void onOk(ActionEvent event) {
        foreach(new PluginRefreshVisitor());
        close();
    }

    @FXML
    void onEnable(ActionEvent event) {
        setSelectedState(true);
        tblPlugins.setItems(rebuild());
    }

    @FXML
    void onDisable(ActionEvent event) {
        setSelectedState(false);
        tblPlugins.setItems(rebuild());
    }

    @FXML
    void onSearch(ActionEvent event) {
        tblPlugins.setItems(rebuild());
    }

    @Override
    public String getTitle() {
        return Messages.getString("pluginOverview.title");
    }

    /**
     * Updates the selected state of a plugin.
     *
     * @param enable true if plugin should be enabled
     */
    private void setSelectedState(boolean enable) {
        ObservableList<PluginModel> selected = tblPlugins.getSelectionModel().getSelectedItems();
        for (PluginModel select : selected) {
            if (select != null) {
                Class<? extends Plugin> plugin = EntityLoader.getPluginFromManifest(select.manifest);
                PluginConfig config = getByName(plugin.getName());
                config.setEnabled(enable);
                select.setState(enable);
                config.update();
                System.out.println("Bean was saved.. " + enable);
            }
        }
    }

    /**
     * Rebuilds the plugin table
     *
     * @return list of entries in the plugin table
     */
    private ObservableList<PluginModel> rebuild() {
        List<Class<? extends Plugin>> plugins = EntityLoader.getPlugins();
        ObservableList<PluginModel> pluginModelList = observableArrayList();
        String filterName = txtName.getText().toLowerCase();
        for (Class<? extends Plugin> plugin : plugins) {
            PluginManifest manifest = plugin.getAnnotation(PluginManifest.class);
            if (filterName.length() == 0 || manifest.name().toLowerCase().contains(filterName)) {
                PluginModel model = new PluginModel(manifest, plugin);
                pluginModelList.add(model);
            }
        }
        return pluginModelList;
    }
}
