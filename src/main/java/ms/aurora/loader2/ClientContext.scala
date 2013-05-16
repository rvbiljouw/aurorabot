package ms.aurora.loader2

import java.applet.{AudioClip, Applet, AppletContext}
import java.util
import java.io.InputStream
import java.net.URL
import java.awt.Image
import ms.aurora.loader2.impl.ClientAudioClip
import javax.imageio.ImageIO

/**
 * @author rvbiljouw
 */
class ClientContext extends AppletContext {

  def getAudioClip(url: URL): AudioClip = new ClientAudioClip(url)

  def getImage(url: URL): Image = ImageIO.read(url)

  def getApplet(name: String): Applet = {
    println("getApplet(): " + name)
    null
  }

  def getApplets: util.Enumeration[Applet] = {
    println("getApplets()");
    null
  }

  def showDocument(url: URL) {}

  def showDocument(url: URL, target: String) {}

  def showStatus(status: String) {}

  def setStream(key: String, stream: InputStream) {}

  def getStream(key: String): InputStream = ???

  def getStreamKeys: util.Iterator[String] = ???

}
