package ms.aurora.gui2.widget

import ms.aurora.gui2.ScalaApplicationGUI
import ms.aurora.gui.util.FXUtils
import javafx.scene.layout.AnchorPane
import java.applet.Applet
import javafx.beans.value.{ObservableValue, ChangeListener}

/**
 * @author rvbiljouw
 */
class ScalaAppletWidget(gui: ScalaApplicationGUI) extends AnchorPane with ChangeListener[Boolean] {
  FXUtils.load(getClass.getResource("AppletWidget.fxml"), this)

  var applet: Applet = null

  def getApplet = applet

  def setApplet(applet: Applet) {
    this.applet = applet
  }

  def changed(p1: ObservableValue[_ <: Boolean], p2: Boolean, p3: Boolean) {

  }
}
