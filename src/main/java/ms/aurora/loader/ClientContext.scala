package ms.aurora.loader

import java.applet.{AudioClip, Applet, AppletContext}
import java.util
import java.io.InputStream
import java.net.URL
import java.awt.Image

/**
 * @author rvbiljouw
 */
class ClientContext extends AppletContext {

  def getAudioClip(url: URL): AudioClip = ???

  def getImage(url: URL): Image = ???

  def getApplet(name: String): Applet = ???

  def getApplets: util.Enumeration[Applet] = ???

  def showDocument(url: URL) {}

  def showDocument(url: URL, target: String) {}

  def showStatus(status: String) {}

  def setStream(key: String, stream: InputStream) {}

  def getStream(key: String): InputStream = ???

  def getStreamKeys: util.Iterator[String] = ???

}
