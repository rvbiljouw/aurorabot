/**
 * Created with IntelliJ IDEA.
 * User: Cov
 * Date: 15/04/13
 * Time: 23:54
 * To change this template use File | Settings | File Templates.
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;

@PluginManifest(name = "Tile Utilities", author = "Cov", version = 1.0)
public class TileUtilityPlugin extends Plugin {

    private PathMaker pathMakerUI = new PathMaker(this);
    private AreaMaker areaMakerUI = new AreaMaker(this);
    private boolean showPathMaker = false, showAreaMaker = false;
    private Menu menu;

    @Override
    public void startup() {
        menu = new Menu("Tile Utilities");

        CheckMenuItem pathMaker = new CheckMenuItem("Toggle Path Maker");
        pathMaker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!showPathMaker) {
                    getSession().getPaintManager().register(pathMakerUI);
                } else {
                    getSession().getPaintManager().deregister(pathMakerUI);
                }
                showPathMaker = !showPathMaker;
                pathMakerUI.setVisible(showPathMaker);
            }
        });
        menu.getItems().add(pathMaker);

        CheckMenuItem interfaces = new CheckMenuItem("Toggle Area Maker");
        interfaces.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!showAreaMaker) {
                    getSession().getPaintManager().register(areaMakerUI);
                } else {
                    getSession().getPaintManager().deregister(areaMakerUI);
                }
                showAreaMaker = !showAreaMaker;
                areaMakerUI.setVisible(showAreaMaker);
            }
        });
        menu.getItems().add(interfaces);

        getSession().registerMenu(menu);
    }

    @Override
    public void execute() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cleanup() {
        getSession().deregisterMenu(menu);
    }
}
