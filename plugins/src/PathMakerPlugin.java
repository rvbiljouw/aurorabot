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

@PluginManifest(name = "Path Maker", author = "Cov", version = 1.0)
public class PathMakerPlugin extends Plugin {

    private PathMaker pathMakerUI = new PathMaker(this);
    private boolean interfacePaintActive = false;
    private Menu menu;

    @Override
    public void startup() {
        menu = new Menu("Path Maker");

        CheckMenuItem interfaces = new CheckMenuItem("Toggle Path Maker");
        interfaces.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!interfacePaintActive) {
                    getSession().getPaintManager().register(pathMakerUI);
                } else {
                    getSession().getPaintManager().deregister(pathMakerUI);
                }
                interfacePaintActive = !interfacePaintActive;
                pathMakerUI.setVisible(interfacePaintActive);
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
