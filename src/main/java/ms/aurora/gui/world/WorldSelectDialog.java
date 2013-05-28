package ms.aurora.gui.world;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import ms.aurora.browser.wrapper.HTML;
import ms.aurora.gui.Dialog;
import ms.aurora.gui.util.FXUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;

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

        WebView webview = new WebView();
        WebEngine engine = webview.getEngine();
        engine.getLoadWorker().stateProperty().addListener(
                browserStateListener(engine));
        engine.load("http://oldschool.runescape.com/slu");
        webview.setMaxSize(0, 0);
        webview.setPrefSize(0, 0);
        getChildren().add(webview);
    }

    @Override
    public String getTitle() {
        return "Select a world";
    }

    private ChangeListener<Worker.State> browserStateListener(final WebEngine webengine) {
        return new ChangeListener<Worker.State>() {
            public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    Document doc = webengine.getDocument();
                    try {
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                        StringWriter sw = new StringWriter();
                        transformer.transform(new DOMSource(doc),
                                new StreamResult(sw));
                        ByteArrayInputStream bis = new ByteArrayInputStream(sw.toString().getBytes("UTF-8"));
                        HTML html = HTML.fromStream(bis);
                        List<Node> worlds = html.searchXPath("//table[@class=\"server\"]/tbody/tr[not(@class=\"slist_header\")]");
                        for (Node world : worlds) {
                            List<Node> values = html.searchXPath(world, "td");

                            WorldModel model = new WorldModel();
                            model.setName(new SimpleStringProperty(values.get(0).getTextContent()));
                            model.setPlayers(new SimpleStringProperty(values.get(1).getTextContent()));
                            model.setCountry(new SimpleStringProperty(values.get(2).getTextContent()));
                            model.setMembers(new SimpleStringProperty(values.get(3).getTextContent()));
                            worldModels.add(model);
                        }
                        tblWorlds.setItems(worldModels);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
    }
}
