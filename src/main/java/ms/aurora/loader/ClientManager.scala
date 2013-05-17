package ms.aurora.loader

import ms.aurora.browser.Browser
import ms.aurora.browser.ContextBuilder.get
import ms.aurora.Messages.getString
import scala.beans.BeanProperty
import java.applet.Applet
import ms.aurora.rt3.Client
import org.apache.log4j.Logger

/**
 * The context of the game client, which acts
 * as some sort of wrapper around the client applet
 * and the loaders / configurations used.
 * @author rvbiljouw
 */
class ClientManager {
  private val logger = Logger.getLogger(classOf[ClientManager])
  @BeanProperty val browserBaseURL = getString("runescape.url")
  @BeanProperty val browserContext = get.domain(browserBaseURL).build
  @BeanProperty val browser = new Browser(browserContext)
  @BeanProperty val config = new ClientConfig(browser)
  @BeanProperty val loader = new ClientLoader(config)

  def getClient: Client = {
    loader.getApplet.asInstanceOf[Client]
  }

  def getApplet: Applet = {
    loader.getApplet
  }

  def start(): Boolean = {
    config.visit()
    loader.start()
    logger.info("Applet started")
    loader.isLoaded
  }

  def restart(): Boolean = {
    if (stop() && start()) {
      logger.info("Applet restarted")
      true
    } else {
      logger.error("Applet reload failed")
      false
    }
  }

  def stop(): Boolean = {
    loader.stop()
    logger.info("Applet stopped")
    !loader.isLoaded
  }

}