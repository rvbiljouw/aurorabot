package ms.aurora.api.javafx;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ms.aurora.api.javafx.FXUtils;

/**
 * @author rvbiljouw
 */
public abstract class Dialog extends AnchorPane {
    protected Stage currentStage = null;

    public abstract String getTitle();

    public void onClose() {

    }

    public void show() {
        currentStage = FXUtils.createModalStage(getTitle(), this);
        currentStage.show();
    }

    public void showAndWait() {
        currentStage = FXUtils.createModalStage(getTitle(), this);
        currentStage.showAndWait();
    }

    public void close() {
        onClose();

        if (currentStage != null) {
            currentStage.close();
        }
    }

}
