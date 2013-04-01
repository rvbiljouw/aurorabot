import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;

/**
 * @author tobiewarburton
 */
@PluginManifest(name = "Interface Debug", author = "tobiewarburton", version = 1.0)
public class InterfaceDebug extends Plugin {
    private InterfaceExplorer explorer = new InterfaceExplorer(this);
    private boolean interfacePaintActive = false;
    private Menu menu;

    @Override
    public void startup() {
        explorer.init();
        menu = new Menu("Interfaces");

        CheckMenuItem interfaces = new CheckMenuItem("Toggle Interface Explorer");
        interfaces.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!interfacePaintActive) {
                    getSession().getPaintManager().register(explorer);
                    explorer.toggle();
                } else {
                    getSession().getPaintManager().deregister(explorer);
                    explorer.toggle();
                }
                interfacePaintActive = !interfacePaintActive;
            }
        });
        menu.getItems().add(interfaces);
        getSession().registerMenu(menu);
    }

    @Override
    public void execute() {
    }

    @Override
    public void cleanup() {
        getSession().deregisterMenu(menu);
    }


}
