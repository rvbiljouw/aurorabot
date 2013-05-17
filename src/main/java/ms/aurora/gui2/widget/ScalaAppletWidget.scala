package ms.aurora.gui2.widget

import ms.aurora.gui2.ScalaApplicationGUI
import ms.aurora.gui.util.FXUtils
import javafx.scene.layout.AnchorPane

/**
 * @author rvbiljouw
 */
class ScalaAppletWidget(gui: ScalaApplicationGUI) extends AnchorPane {
  FXUtils.load(getClass.getResource("AppletWidget.fxml"), this)

}
