package ms.aurora.core

import ms.aurora.gui.widget.AppletWidget
import scala.beans.BeanProperty
import ms.aurora.loader.ClientManager
import org.apache.log4j.Logger
import akka.actor.{Props, ActorSystem}
import ms.aurora.core.script.{TickEvent, ScriptSupervisor}
import ms.aurora.gui.ApplicationGUI
import ms.aurora.event.PaintManager
import javafx.scene.control.Menu
import ms.aurora.core.model.Account

/**
 * Represents one client session
 * @author rvbiljouw
 */
class Session(group: ThreadGroup, ui: AppletWidget) extends Runnable {
  private val logger = Logger.getLogger(classOf[Session])
  @BeanProperty val actorSystem = ActorSystem("Session")
  @BeanProperty val scriptSupervisor = actorSystem.actorOf(
    Props(classOf[ScriptSupervisor], this), name = "scriptSupervisor")
  @BeanProperty val clientManager = new ClientManager()
  @BeanProperty val paintManager = new PaintManager()
  @BeanProperty val account: Account = null
  var active = false

  override def run() {
    while (!Thread.currentThread().isInterrupted) {

      if (clientManager.getApplet == null) {
        if (clientManager.start()) {
          ui.setApplet(clientManager.getApplet)
        } else {
          logger.error("Failed to initialize applet, killing..")
          Thread.currentThread().interrupt()
        }
      } else {
        scriptSupervisor ! TickEvent()
        Thread.sleep(250)
      }

    }
  }

  def getThreadGroup = group

  def getActive = active

  def setActive(active: Boolean) {
    this.active = active
    active match {
      case true => {
        logger.info("Set to active")
        ApplicationGUI.update()
      }

      case false => {
        logger.info("Set to non-active")
        ApplicationGUI.setInput(true)
        ApplicationGUI.update()
      }
    }
  }

  def getName = "No name"

  def registerMenu(menu: Menu) {
    logger.info("Registering plugin menus temporarily disabled")
  }

  def deregisterMenu(menu: Menu) {
    logger.info("Deregistering plugin menus temporarily disabled")
  }
}
