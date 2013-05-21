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
@PluginManifest(name = "Interface Utilities", shortDescription = "Contains an interface explorer", author = "vim", version = 0.1)
public class InterfacePlugin extends Plugin {
    private Menu menu;
    private InterfaceExplorer interfaceExplorer = new InterfaceExplorer(this);
    private InterfaceExplorerStage interfaceExplorerStage;
    private CheckMenuItem explorerToggleCheckbox;

    @Override
    public void startup() {

        menu = new Menu("Interface Utilities");

        explorerToggleCheckbox = new CheckMenuItem("Toggle Explorer");

        explorerToggleCheckbox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (interfaceExplorerStage != null && interfaceExplorerStage.isShowing()) {
                    interfaceExplorerStage.hide();
                    getSession().getPaintManager().deregister(interfaceExplorer);
                } else {
                    if (interfaceExplorerStage == null) {
                        interfaceExplorerStage = new InterfaceExplorerStage();
                    }
                    interfaceExplorerStage.show();
                    getSession().getPaintManager().register(interfaceExplorer);
                }
            }
        });

        menu.getItems().add(explorerToggleCheckbox);

        registerMenu(menu);
    }

    @Override
    public void execute() {
    }

    @Override
    public void cleanup() {
        if (interfaceExplorerStage != null) {
            interfaceExplorerStage.hide();
        }
        deregisterMenu(menu);
    }

    class InterfaceExplorerStage extends Stage {
        public InterfaceExplorerStage() {
            initOwner(null);
            setTitle("Aurora Interface Tools");
            setMinWidth(600);
            setMinHeight(400);
            setResizable(false);
            setScene(new Scene(interfaceExplorer));
            setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    explorerToggleCheckbox.setSelected(false);
                }
            });
            centerOnScreen();
        }
    }
}
