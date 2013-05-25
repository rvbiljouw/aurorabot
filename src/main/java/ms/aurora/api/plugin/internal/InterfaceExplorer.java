package ms.aurora.api.plugin.internal;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import ms.aurora.api.Context;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author tobiewarburton
 */
public class InterfaceExplorer extends AnchorPane implements PaintListener {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnReload;

    @FXML
    private TableColumn<InterfaceModel, String> clmProperty;

    @FXML
    private TableColumn<InterfaceModel, String> clmValue;

    @FXML
    private TableView<InterfaceModel> tblInfo;

    @FXML
    private TreeView<RSWidget> treeView;

    private InterfacePlugin plugin;

    private int x, y, w, h;

    public InterfaceExplorer(InterfacePlugin plugin) {
        this.plugin = plugin;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InterfaceExplorer.fxml"));

        fxmlLoader.setController(this);

        try {
            this.getChildren().add((Parent) fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void onReload(ActionEvent event) {
        Context.invokeLater(plugin.session.getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                RSWidgetGroup[] all = Widgets.getAll();
                reload(all);
            }
        });
    }

    @FXML
    void initialize() {
        assert btnReload != null : "fx:id=\"btnReload\" was not injected: check your FXML file 'InterfaceExplorer.fxml'.";
        assert clmProperty != null : "fx:id=\"clmProperty\" was not injected: check your FXML file 'InterfaceExplorer.fxml'.";
        assert clmValue != null : "fx:id=\"clmValue\" was not injected: check your FXML file 'InterfaceExplorer.fxml'.";
        assert tblInfo != null : "fx:id=\"tblInfo\" was not injected: check your FXML file 'InterfaceExplorer.fxml'.";
        assert treeView != null : "fx:id=\"treeView\" was not injected: check your FXML file 'InterfaceExplorer.fxml'.";

        clmProperty.setCellValueFactory(new PropertyValueFactory<InterfaceModel, String>("property"));
        clmValue.setCellValueFactory(new PropertyValueFactory<InterfaceModel, String>("value"));

        treeView.setShowRoot(false);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeView.getSelectionModel().selectedItemProperty().addListener(new TreeViewChangeListener());
    }

    private void reload(final RSWidgetGroup[] widgetGroups) {
        if (widgetGroups == null) return;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TreeItem<RSWidget> rootItem = new TreeItem<RSWidget>();
                for (RSWidgetGroup group : widgetGroups) {
                    if (group == null || group.getWidgets().length == 0) continue;
                    RSWidget[] groupItems = group.getWidgets();
                    TreeItem<RSWidget> interfaceRoot = new TreeItem<RSWidget>(groupItems[0]);
                    for (int i = 1; i < groupItems.length; i++) {
                        if (groupItems[i] != null) {
                            TreeItem<RSWidget> groupItem = new TreeItem<RSWidget>(groupItems[i]);
                            for (RSWidget actualChild : groupItems[i].getChildren()) {
                                if (actualChild != null) {
                                    groupItem.getChildren().add(new TreeItem<RSWidget>(actualChild));
                                }
                            }
                            interfaceRoot.getChildren().add(groupItem);
                        }
                    }
                    rootItem.getChildren().add(interfaceRoot);
                    rootItem.setExpanded(true);
                }
                treeView.setRoot(rootItem);
            }
        });
    }

    public class InterfaceModel {
        private SimpleStringProperty property;
        private SimpleStringProperty value;

        public InterfaceModel(String property, Object value) {
            this.property = new SimpleStringProperty(property);
            this.value = new SimpleStringProperty(value.toString());
        }

        public String getProperty() {
            return property.get();
        }

        public String getValue() {
            return value.get();
        }
    }

    @Override
    public void onRepaint(Graphics graphics) {
        if (x != -1 && y != -1) {
            graphics.setColor(Color.WHITE);
            graphics.drawRect(x, y, w, h);
        }
    }

    class TreeViewChangeListener implements ChangeListener<TreeItem<RSWidget>> {
        @Override
        public void changed(ObservableValue<? extends TreeItem<RSWidget>> observableValue, TreeItem<RSWidget> oldValue, TreeItem<RSWidget> newValue) {
            if (newValue == null || newValue.getValue() == null) return;
            final RSWidget widget = newValue.getValue();
            plugin.invokeLater(new Runnable() { // so context is set !
                @Override
                public void run() {
                    final ArrayList<InterfaceModel> props = new ArrayList<InterfaceModel>();
                    props.add(new InterfaceModel("x", widget.getX()));
                    props.add(new InterfaceModel("y", widget.getY()));
                    props.add(new InterfaceModel("width", widget.getWidth()));
                    props.add(new InterfaceModel("height", widget.getHeight()));
                    props.add(new InterfaceModel("text", widget.getText()));
                    props.add(new InterfaceModel("type", widget.getType()));
                    props.add(new InterfaceModel("model", widget.getModelId()));
                    if (widget.getChildren() != null ) {
                        props.add(new InterfaceModel("children count", widget.getChildren().length));
                    }
                    if (widget.getInventoryItems() != null) {
                        props.add(new InterfaceModel("items", widget.getInventoryItems().length));
                    }

                    x = widget.getX();
                    y = widget.getY();
                    w = widget.getWidth();
                    h = widget.getHeight();

                    Platform.runLater(new Runnable() { // so javafx doesnt bitch
                        @Override
                        public void run() {
                            tblInfo.setItems(observableArrayList(props));
                        }
                    });
                }
            });
        }
    }
}
