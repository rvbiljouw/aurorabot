package ms.aurora.gui.script;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.core.Repository;
import ms.aurora.core.Session;
import ms.aurora.core.script.EntityLoader;
import ms.aurora.gui.Dialog;
import ms.aurora.gui.Messages;
import ms.aurora.gui.dialog.AccountSelectDialog;
import ms.aurora.gui.util.FXUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static ms.aurora.gui.Main.getSelectedApplet;

/**
 * @author Rick
 */
public class ScriptOverview extends Dialog {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private ComboBox<String> cbxCategory;
    @FXML
    private TableView<ScriptModel> tblScripts;
    @FXML
    private TableColumn<ScriptModel, String> colAuthor;
    @FXML
    private TableColumn<ScriptModel, String> colCategory;
    @FXML
    private TableColumn<ScriptModel, String> colName;
    @FXML
    private TableColumn<ScriptModel, String> colShortDesc;
    @FXML
    private TextField txtName;

    public ScriptOverview() {
        FXUtils.load(getClass().getResource("ScriptOverview.fxml"), this);
    }

    @FXML
    void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<ScriptModel, String>("name"));
        colShortDesc.setCellValueFactory(new PropertyValueFactory<ScriptModel, String>("shortDesc"));
        colCategory.setCellValueFactory(new PropertyValueFactory<ScriptModel, String>("category"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<ScriptModel, String>("author"));
        ObservableList<String> categoryList = FXCollections.observableArrayList();
        categoryList.add(Messages.getString("scriptOverview.all"));


        tblScripts.setItems(rebuild());
        cbxCategory.setItems(categoryList);
        cbxCategory.getSelectionModel().selectFirst();
    }

    @FXML
    void onOk(ActionEvent event) {
        final ScriptModel model = tblScripts.getSelectionModel().selectedItemProperty().getValue();
        final Session session = Repository.get(getSelectedApplet().hashCode());
        if (session != null && model != null) {
            final AccountSelectDialog selector = new AccountSelectDialog();
            selector.showAndWait();
            session.setAccount(selector.get());
            session.getScriptManager().start(model.script);
            close();
        } else {
            close();
        }
    }

    @FXML
    void onCancel() {
        getScene().getWindow().hide();
    }

    @FXML
    void onSearch() {
        tblScripts.setItems(rebuild());
    }

    @Override
    public String getTitle() {
        return Messages.getString("scriptOverview.title");
    }

    /**
     * Reloads the entire script table contents
     *
     * @return list of items for the script table.
     */
    private ObservableList<ScriptModel> rebuild() {
        List<Class<? extends Script>> scripts = EntityLoader.getScripts();
        ObservableList<ScriptModel> scriptModelList = FXCollections.observableArrayList();
        String selectedCategory = cbxCategory.getSelectionModel().getSelectedItem();
        String filterName = txtName.getText().toLowerCase();

        for (Class<? extends Script> script : scripts) {
            ScriptManifest manifest = script.getAnnotation(ScriptManifest.class);
            if (selectedCategory == null || selectedCategory.equals(Messages.getString("scriptOverview.all"))
                    || selectedCategory.equals(manifest.category())) {

                if (filterName.length() == 0 || manifest.name().toLowerCase().contains(filterName)) {
                    scriptModelList.add(new ScriptModel(script, manifest));
                }
            }
        }
        return scriptModelList;
    }
}
