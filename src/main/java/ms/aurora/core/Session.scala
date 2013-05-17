package ms.aurora.core

import ms.aurora.gui.widget.AppletWidget
import scala.beans.BeanProperty
import ms.aurora.loader.ClientManager
import org.apache.log4j.Logger
import akka.actor.{ActorSystem, Actor, ActorRef, Props}
import ms.aurora.gui.ApplicationGUI
import ms.aurora.event.PaintManager
import javafx.scene.control.Menu
import ms.aurora.core.model.Account
import ms.aurora.core.actor.{SessionBridge, ScriptSupervisor}

/**
 * Represents one client session
 * @author rvbiljouw
 */
class Session(group: ThreadGroup, ui: AppletWidget) extends Runnable {
  private val logger = Logger.getLogger(classOf[Session])
  private val actorSystem = ActorSystem("sessionsystem")
  @BeanProperty val clientManager = new ClientManager()
  @BeanProperty val paintManager = new PaintManager()
  @BeanProperty var sessionBridge: ActorRef = null
  @BeanProperty var scriptSupervisor: ActorRef = null
  @BeanProperty val account: Account = null
  var active = false

  override def run() {
    if (clientManager.start()) {
      SessionRepository.set(clientManager.getApplet.hashCode, this)
      ui.setApplet(clientManager.getApplet)
    }

    scriptSupervisor = actorSystem.actorOf(
      Props(classOf[ScriptSupervisor], this))
    sessionBridge = actorSystem.actorOf(
      Props(classOf[SessionBridge], this))
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

  def getActorSystem: ActorSystem = actorSystem

}
