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

/**
 * @author rvbiljouw
 */
public class WorldSelectDialog extends Dialog {
    private final ObservableList<WorldModel> worldModels = FXCollections.observableArrayList();
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
        tblWorlds.setItems(worldModels);

        Browser browser = new Browser(new ContextBuilder().domain("oldschool.runescape.com").build());
        GetRequest request = new GetRequest("/slu");
        browser.doRequest(request, new ResponseHandler() {
            @Override
            public void handleResponse(InputStream inputStream) {
                try {
                    Plaintext text = Plaintext.fromStream(inputStream);
                  //  e(306,true,0,"oldschool6",274,"United States","US","Old School 6");
                    Matcher m = text.regex(".\\((.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?),(.*?)\\);");
                    while(m.find()) {
                        int worldNumber = Integer.parseInt(m.group(1));
                        boolean members = parseBoolean(m.group(2));
                        String worldIdent = m.group(4).replaceAll("\"", "");
                        int players = parseInt(m.group(5));
                        String country = m.group(6).replaceAll("\"", "");
                        String worldName = m.group(8).replaceAll("\"", "");

                        WorldModel model = new WorldModel();
                        model.setName(worldName + " (" + worldNumber + ")");
                        model.setCountry(country);
                        model.setMembers(members);
                        model.setWorldNo(worldNumber);
                        model.setPlayers(players);
                        model.setIdent(worldIdent);

                        worldModels.add(model);
                    }
                    tblWorlds.setItems(worldModels);
                } catch (ParsingException e) {
                    e.printStackTrace();
                }
            }
        });
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
