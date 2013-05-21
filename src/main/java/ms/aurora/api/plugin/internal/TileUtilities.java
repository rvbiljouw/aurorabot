package ms.aurora.api.plugin.internal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
    private CheckMenuItem pathMakerCheckbox;

    @Override
    public void startup() {

        menu = new Menu("Tile Utilities");

        pathMakerCheckbox = new CheckMenuItem("Toggle Path Maker");

        pathMakerCheckbox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (pathMakerStage != null && pathMakerStage.isShowing()) {
                    pathMakerStage.hide();
                    pathMaker.stop();
                } else {
                    if (pathMakerStage == null) {
                        pathMakerStage = new PathMakerStage();
                    }
                    pathMakerStage.show();
                }
            }
        });

        menu.getItems().add(pathMakerCheckbox);
        registerMenu(menu);
    }

    @Override
    public void execute() {
    }

    @Override
    public void cleanup() {
        if (pathMakerStage != null) {
            pathMakerStage.hide();
        }
        deregisterMenu(menu);
    }

    class PathMakerStage extends Stage {
        public PathMakerStage() {
            initOwner(null);
            setTitle("Aurora Path Maker");
            setMinWidth(300);
            setMinHeight(400);
            setResizable(false);
            setScene(new Scene(pathMaker));
            setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    pathMakerCheckbox.setSelected(false);
                }
            });
            centerOnScreen();
        }
    }
}
