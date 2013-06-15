package ms.aurora.api.javafx;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ms.aurora.gui.Messages;

import java.io.IOException;
import java.net.URL;

/**
 * @author rvbiljouw
 */
public class FXUtils {

    /**
     * Creates and shows a modal dialog
     *
     * @param title the title of the dialog
     * @param parent the parent object
     */
    public static void showModalDialog(String title, Parent parent) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(null);
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(Messages.getString("gui.theme"));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Creates a simple modal stage
     *
     * @param title the title of the dialog
     * @param parent the parent object
     * @return the stage in which is configured to be a modal
     */
    public static Stage createModalStage(String title, Parent parent) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(null);
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(Messages.getString("gui.theme"));
        stage.setScene(scene);
        return stage;
    }

    /**
     * Shows a modal dialog with a close handler.
     *
     * @param title the title of the dialog
     * @param parent the parent object
     * @param closeHandler the handler in which is called on close
     */
    public static void showModalDialog(String title, Parent parent, EventHandler<WindowEvent> closeHandler) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(null);
        stage.setOnCloseRequest(closeHandler);
        stage.setOnHidden(closeHandler);
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(Messages.getString("gui.theme"));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
    public static void load(URL resource, Parent object) {
        FXMLLoader fxmlLoader = new FXMLLoader(resource, Messages.getBundle());
        fxmlLoader.setRoot(object);
        fxmlLoader.setController(object);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("Loading failed", e);
        }
    }

}
