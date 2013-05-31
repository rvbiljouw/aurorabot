package ms.aurora.gui.world;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ms.aurora.api.javafx.Dialog;
import ms.aurora.api.javafx.FXUtils;
import ms.aurora.api.methods.web.Worlds;
import ms.aurora.api.methods.web.model.World;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author rvbiljouw
 */
public class WorldSelectDialog extends Dialog {
    @FXML
    private TableColumn<World, String> colCountry;
    @FXML
    private TableColumn<World, String> colMembers;
    @FXML
    private TableColumn<World, String> colName;
    @FXML
    private TableColumn<World, String> colPlayers;
    @FXML
    private TableView<World> tblWorlds;

    public WorldSelectDialog() {
        FXUtils.load(getClass().getResource("WorldSelectDialog.fxml"), this);
    }

    @FXML
    public void initialize() {
        colCountry.setCellValueFactory(new PropertyValueFactory<World, String>("country"));
        colMembers.setCellValueFactory(new PropertyValueFactory<World, String>("members"));
        colName.setCellValueFactory(new PropertyValueFactory<World, String>("name"));
        colPlayers.setCellValueFactory(new PropertyValueFactory<World, String>("players"));
        tblWorlds.setItems(observableArrayList(Worlds.getAll()));
    }

    @FXML
    void onOk() {
        close();
    }

    public World getSelected() {
        return tblWorlds.getSelectionModel().getSelectedItem();
    }

    @Override
    public String getTitle() {
        return "Select a world";
    }
}
