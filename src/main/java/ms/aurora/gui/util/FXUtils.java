package ms.aurora.gui.util;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ms.aurora.Application;

/**
 * @author rvbiljouw
 */
public class FXUtils {

    public static void showModalDialog(String title, Parent parent) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(Application.mainStage);
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("soft-responsive.css");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static Stage createModalStage(String title, Parent parent) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(Application.mainStage);
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("soft-responsive.css");
        stage.setScene(scene);
        return stage;
    }

    public static void showModalDialog(String title, Parent parent, EventHandler<WindowEvent> closeHandler) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(Application.mainStage);
        stage.setOnCloseRequest(closeHandler);
        stage.setOnHidden(closeHandler);
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("soft-responsive.css");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

}
