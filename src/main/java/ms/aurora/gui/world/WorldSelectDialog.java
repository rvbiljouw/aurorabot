package ms.aurora.gui.world;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ms.aurora.browser.Browser;
import ms.aurora.browser.ContextBuilder;
import ms.aurora.browser.ResponseHandler;
import ms.aurora.browser.exception.ParsingException;
import ms.aurora.browser.impl.GetRequest;
import ms.aurora.browser.wrapper.Plaintext;
import ms.aurora.gui.Dialog;
import ms.aurora.gui.util.FXUtils;

import java.io.InputStream;
import java.util.regex.Matcher;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author rvbiljouw
 */
public class WorldSelectDialog extends Dialog {
    @FXML
    private TableColumn<WorldModel, String> colCountry;
    @FXML
    private TableColumn<WorldModel, String> colMembers;
    @FXML
    private TableColumn<WorldModel, String> colName;
    @FXML
    private TableColumn<WorldModel, String> colPlayers;
    @FXML
    private TableView<WorldModel> tblWorlds;

    public WorldSelectDialog() {
        FXUtils.load(getClass().getResource("WorldSelectDialog.fxml"), this);
    }

    @FXML
    public void initialize() {
        colCountry.setCellValueFactory(new PropertyValueFactory<WorldModel, String>("country"));
        colMembers.setCellValueFactory(new PropertyValueFactory<WorldModel, String>("members"));
        colName.setCellValueFactory(new PropertyValueFactory<WorldModel, String>("name"));
        colPlayers.setCellValueFactory(new PropertyValueFactory<WorldModel, String>("players"));
        WorldModel.load();
        tblWorlds.setItems(observableArrayList(WorldModel.WORLDS));
    }

    @FXML
    void onOk() {
        close();
    }

    public WorldModel getSelected() {
        return tblWorlds.getSelectionModel().getSelectedItem();
    }

    @Override
    public String getTitle() {
        return "Select a world";
    }
}
