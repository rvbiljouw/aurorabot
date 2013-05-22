package ms.aurora.gui;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ms.aurora.gui.util.FXUtils;

/**
 * @author rvbiljouw
 */
public abstract class Dialog extends AnchorPane {
    private Stage currentStage = null;

    public abstract String getTitle();

    public void show() {
        currentStage = FXUtils.createModalStage(getTitle(), this);
        currentStage.show();
    }

    public void showAndWait() {
        currentStage = FXUtils.createModalStage(getTitle(), this);
        currentStage.showAndWait();
    }

    public void close() {
        currentStage.close();
    }

}
