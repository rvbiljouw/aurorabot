package ms.aurora.gui2

import javafx.scene.layout.AnchorPane
import javafx.fxml.FXML
import ms.aurora.gui.util.FXUtils
import javafx.scene.control.{ToggleButton, TabPane}
import ms.aurora.event.GlobalEventQueue.blocking
import ms.aurora.core.{Repository, Session}
import ms.aurora.gui.widget.AppletWidget
import ms.aurora.gui2.widget.ScalaAppletWidget

/**
 * The main application GUI controller
 * @author rvbiljouw
 */
class ScalaApplicationGUI extends AnchorPane {
  FXUtils.load(getClass.getResource("ApplicationGUI.fxml"), this)
  @FXML var btnInput: ToggleButton = null
  @FXML var tabPane: TabPane = null

  def onNewSession() {
    val widget = new ScalaAppletWidget(this)
    val group = new ThreadGroup("group")
  }

  def onStartScript() {

  }

  def onPauseScript() {

  }

  def onResumeScript() {

  }

  def onStopScript() {

  }

  def onToggleInput() {
    blocking = btnInput.isSelected
  }

  def onClose() {
    System.exit(0)
  }

  def getSession: Session = {
    val selectedObject = tabPane.getSelectionModel.getSelectedItem.getContent
    if (selectedObject.isInstanceOf[AppletWidget]) {
      Repository.all().filter(
        _.getClientManager.getApplet.eq(
          selectedObject.asInstanceOf[AppletWidget].getApplet
        )
      ).headOption.getOrElse(null)
    }
    null
  }

}
