package ms.aurora.loader

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

  def getDocumentBase: URL =
    new URL(config.getDocumentBase)

  def getCodeBase: URL =
    new URL(config.getDocumentBase)

  def getParameter(name: String): String =
    config.getParam(name)

  def getAppletContext: AppletContext =
    new ClientContext()

  def appletResize(width: Int, height: Int) {}
}
