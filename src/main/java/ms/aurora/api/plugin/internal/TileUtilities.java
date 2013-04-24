package ms.aurora.api.plugin.internal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.stage.Stage;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;

/**
 * @author tobiewarburton
 */
@PluginManifest(name = "Tile Utilities", shortDescription = "Path Maker, aids in constructing paths.", author = "vim", version = 0.1)
public class TileUtilities extends Plugin {
    private Menu menu;
    private PathMaker pathMaker = new PathMaker(this);
    private PathMakerStage pathMakerStage;

    @Override
    public void startup() {

        menu = new Menu("Tile Utilities");

        CheckMenuItem pathMakerCheckbox = new CheckMenuItem("Toggle Path Maker");
        pathMakerCheckbox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (pathMakerStage != null && pathMakerStage.isShowing()) {
                    pathMakerStage.hide();
                    pathMaker.stop();
                } else {
                    pathMakerStage = new PathMakerStage();
                    pathMakerStage.show();
                }
            }
        });

        menu.getItems().add(pathMakerCheckbox);

        getSession().registerMenu(menu);
    }

    @Override
    public void execute() {
    }

    @Override
    public void cleanup() {
        pathMakerStage.hide();
        getSession().deregisterMenu(menu);
    }

    class PathMakerStage extends Stage {
        public PathMakerStage() {
            setTitle("Aurora Path Maker");
            setMinWidth(300);
            setMinHeight(400);
            setResizable(false);
            setScene(new Scene(pathMaker));
            centerOnScreen();
        }
    }
}
