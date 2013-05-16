package ms.aurora.core

import ms.aurora.gui.widget.AppletWidget
import scala.beans.BeanProperty
import ms.aurora.loader2.ClientManager
import org.apache.log4j.Logger

/**
 * Represents one client session
 * @author rvbiljouw
 */
class ScalaSession(ui: AppletWidget) extends Runnable {
  private val logger = Logger.getLogger(classOf[ScalaSession])
  @BeanProperty val clientManager = new ClientManager()

  def run() {
    logger.info("Initializing session..")
    if (clientManager.start()) {
      ui.setApplet(clientManager.getApplet)
      Thread.sleep(5000)
      if(clientManager.restart()) {
        ui.setApplet(clientManager.getApplet)
        Thread.sleep(5000)
        clientManager.stop()
      }

    }
  }

}
