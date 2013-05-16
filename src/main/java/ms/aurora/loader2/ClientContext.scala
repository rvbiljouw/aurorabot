package ms.aurora.loader2

import ms.aurora.browser.Browser
import ms.aurora.browser.ContextBuilder.get
import ms.aurora.Messages.getString
import scala.beans.BeanProperty
import java.applet.Applet

/**
 * The context of the game client, which acts
 * as some sort of wrapper around the client applet
 * and the loaders / configurations used.
 * @author rvbiljouw
 */
class ClientContext {
  @BeanProperty val browserBaseURL = getString("runescape.url")
  @BeanProperty val browserContext = get.domain(browserBaseURL).build
  @BeanProperty val browser = new Browser(browserContext)
  @BeanProperty val config = new ClientConfig(browser)
  @BeanProperty val loader = new ClientLoader()

  def getApplet: Applet = {
    loader.getApplet
  }

  def start() {

  }

  def restart() {
    stop()
    start()
  }

  def stop() {

  }

}
