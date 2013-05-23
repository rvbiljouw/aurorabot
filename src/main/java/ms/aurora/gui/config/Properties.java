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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ms.aurora.core.model.Source;
import ms.aurora.gui.Dialog;
import ms.aurora.gui.Messages;
import ms.aurora.input.ClientCanvas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Properties extends Dialog {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button btnAddScriptSource;
    @FXML
    private Button btnRemoveScriptSource;
    @FXML
    private ListView<Source> lstSources;
    @FXML
    private Slider sldPaintDelay;
    private Stage currentStage;

    public Properties() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Properties.fxml"),
                Messages.getBundle());

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void onAddSource(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(Messages.getString("properties.selectFolder"));
        File file = chooser.showDialog(currentStage);
        if (file != null && file.exists()) {
            Source source = new Source(file.getAbsolutePath(), false);
            source.save();
        }
        loadSources();
    }

    @FXML
    void onOk(ActionEvent event) {
        close();
    }

    @FXML
    void onRemoveSource(ActionEvent event) {
        ObservableList<Source> selection = lstSources.getSelectionModel().getSelectedItems();
        for (Source source : selection) {
            source.remove();
        }
        loadSources();
    }

    @FXML
    void initialize() {
        sldPaintDelay.setValue(ClientCanvas.PAINT_DELAY);
        sldPaintDelay.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                ClientCanvas.PAINT_DELAY = number2.intValue();
            }
        });
        loadSources();
    }

    private void loadSources() {
        lstSources.getItems().clear();
        List<Source> sources = Source.getAll();
        for (Source source : sources) {
            lstSources.getItems().add(source);
        }
    }

    @Override
    public String getTitle() {
        return Messages.getString("properties.title");
    }
}
