package ms.aurora.gui.config;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ms.aurora.Messages;
import ms.aurora.core.model.PluginSource;
import ms.aurora.core.model.ScriptSource;
import ms.aurora.gui.util.FXUtils;
import ms.aurora.input.ClientCanvas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Properties extends AnchorPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAddScriptSource;

    @FXML
    private Button btnRemoveScriptSource;

    @FXML
    private ListView<PluginSource> lstPluginSources;

    @FXML
    private ListView<ScriptSource> lstScriptSources;

    @FXML
    private Slider sldPaintDelay;

    private Stage currentStage;

    public Properties() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Properties.fxml"), Messages.getBundle());

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void onAddPluginSource(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(Messages.getString("properties.selectFolder"));
        File file = chooser.showDialog(currentStage);
        if(file != null && file.exists()) {
            PluginSource source = new PluginSource(file.getAbsolutePath(), false);
            source.save();
        }
        loadPluginSources();
    }

    @FXML
    void onAddScriptSource(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(Messages.getString("properties.selectFolder"));
        File file = chooser.showDialog(currentStage);
        if(file != null && file.exists()) {
            ScriptSource source = new ScriptSource(file.getAbsolutePath(), false);
            source.save();
        }
        loadScriptSources();
    }

    @FXML
    void onOk(ActionEvent event) {
        currentStage.close();
    }

    @FXML
    void onRemovePluginSource(ActionEvent event) {
        ObservableList<PluginSource> selection = lstPluginSources.getSelectionModel().getSelectedItems();
        for(PluginSource source : selection) {
            source.remove();
        }
        loadPluginSources();
    }

    @FXML
    void onRemoveScriptSource(ActionEvent event) {
        ObservableList<ScriptSource> selection = lstScriptSources.getSelectionModel().getSelectedItems();
        for(ScriptSource source : selection) {
            source.remove();
        }
        loadScriptSources();
    }

    @FXML
    void initialize() {
        assert btnAddScriptSource != null : "fx:id=\"btnAddScriptSource\" was not injected: check your FXML file 'Properties.fxml'.";
        assert btnRemoveScriptSource != null : "fx:id=\"btnRemoveScriptSource\" was not injected: check your FXML file 'Properties.fxml'.";
        assert lstPluginSources != null : "fx:id=\"lstPluginSources\" was not injected: check your FXML file 'Properties.fxml'.";
        assert lstScriptSources != null : "fx:id=\"lstScriptSources\" was not injected: check your FXML file 'Properties.fxml'.";
        assert sldPaintDelay != null : "fx:id=\"sldPaintDelay\" was not injected: check your FXML file 'Properties.fxml'.";

        sldPaintDelay.setValue(ClientCanvas.PAINT_DELAY);
        sldPaintDelay.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                ClientCanvas.PAINT_DELAY = number2.intValue();
            }
        });
        loadScriptSources();
        loadPluginSources();
    }

    private void loadScriptSources() {
        lstScriptSources.getItems().clear();
        List<ScriptSource> sources = ScriptSource.getAll();
        for(ScriptSource source : sources) {
            lstScriptSources.getItems().add(source);
        }
    }
    
    private void loadPluginSources() {
        lstPluginSources.getItems().clear();
        List<PluginSource> sources = PluginSource.getAll();
        for(PluginSource source : sources) {
            lstPluginSources.getItems().add(source);
        }
    }

    public void open() {
        currentStage = FXUtils.createModalStage(Messages.getString("properties.title"), this);
        currentStage.show();
    }

}
