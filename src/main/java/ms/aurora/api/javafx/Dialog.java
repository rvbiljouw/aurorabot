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

    /**
     * shows the dialog
     */
    public void show() {
        currentStage = FXUtils.createModalStage(getTitle(), this);
        currentStage.show();
    }

    /**
     * shows the dialog and then blocks until it is closed.
     */
    public void showAndWait() {
        currentStage = FXUtils.createModalStage(getTitle(), this);
        currentStage.showAndWait();
    }

    /**
     * closes the dialog
     */
    public void close() {
        onClose();

        if (currentStage != null) {
            currentStage.close();
        }
    }

}
