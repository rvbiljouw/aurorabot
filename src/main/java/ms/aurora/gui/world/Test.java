package ms.aurora.gui.world;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ms.aurora.gui.Dialog;

/**
 * @author rvbiljouw
 */
public class Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Dialog d = new WorldSelectDialog();
                d.showAndWait();
            }
        });    }
}
