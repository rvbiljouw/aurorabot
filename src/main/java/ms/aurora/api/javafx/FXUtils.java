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
