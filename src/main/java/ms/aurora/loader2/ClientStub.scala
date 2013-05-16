package ms.aurora.loader2

import java.applet.{AppletContext, AppletStub}
import java.net.URL

/**
 * Applet stub provided to the game applet.
 * @author rvbiljouw
 */
class ClientStub(config: ClientConfig) extends AppletStub {
  private var active = false

  def isActive: Boolean = active

  def setActive(active: Boolean) {
    this.active = active
  }

  def getDocumentBase: URL = ???

  def getCodeBase: URL = ???

  def getParameter(name: String): String = ???

  def getAppletContext: AppletContext = ???

  def appletResize(width: Int, height: Int) {}
}
