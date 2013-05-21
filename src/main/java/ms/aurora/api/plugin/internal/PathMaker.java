package ms.aurora.api.plugin.internal;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Players;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.wrappers.RSTile;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;


/**
 * @author tobiewarburton
 */
public class PathMaker extends AnchorPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCopy;

    @FXML
    private ListView<RSTile> listTiles;

    @FXML
    private ToggleButton tglStartStop;

    private Plugin plugin;
    private Thread thread;
    private RSTile last;
    private ArrayList<RSTile> tiles = new ArrayList<RSTile>();

    public PathMaker(Plugin plugin) {
        this.plugin = plugin;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PathMaker.fxml"));

        fxmlLoader.setController(this);

        try {
            this.getChildren().add((Parent) fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void onCopy(ActionEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append("final RSTile[] PATH = new RSTile[] {");
        for (int i = 0; i < tiles.size(); i++) {
            RSTile current = tiles.get(i);
            if (i % 4 == 0) {
                builder.append("\n\t");
            }
            builder.append(String.format("new RSTile(%s, %s), ", current.getX(), current.getY()));
        }
        builder.append("\n};");
        StringSelection selection = new StringSelection(builder.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        System.out.println("Tiles copied to clipboard.");
    }

    @FXML
    void onToggle(ActionEvent event) {
        ToggleButton button = (ToggleButton) event.getSource();
        if (!button.isSelected()) {
            stop();
            button.setText("Start");
        } else {
            start();
            button.setText("Stop");
        }
    }

    public void start() {
        clear();
        thread = new Thread(new Recorder());
        thread.start();
    }

    public void stop() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    @FXML
    void initialize() {
        assert btnCopy != null : "fx:id=\"btnCopy\" was not injected: check your FXML file 'PathMaker.fxml'.";
        assert listTiles != null : "fx:id=\"listTiles\" was not injected: check your FXML file 'PathMaker.fxml'.";
        assert tglStartStop != null : "fx:id=\"tglStartStop\" was not injected: check your FXML file 'PathMaker.fxml'.";
    }

    class Recorder implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (last == null) {
                    add(Players.getLocal().getLocation());
                }
                if (Calculations.distance(last, Players.getLocal().getLocation()) > 6) {
                    add(Players.getLocal().getLocation());
                }
            }
        }
    }

    private void add(RSTile tile) {
        last = tile;
        tiles.add(tile);
        update();
        System.out.println("Added: " + tile);
    }

    private void clear() {
        tiles.clear();
        update();
        System.out.println("Cleared tiles.");
    }

    private void update() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listTiles.setItems(observableArrayList(tiles));
            }
        });
    }
}
