package ms.aurora.loader2

import java.applet.Applet
import scala.beans.BeanProperty
import org.apache.log4j.Logger
import java.net.{JarURLConnection, URL}
import ms.aurora.transform.ClientClassLoader
import ms.aurora.sdn.net.api.Hooks.getHooks

/**
 * Applet loader responsible for loading the client
 * based on a ClientConfiguration.
 * @author rvbiljouw
 */
class ClientLoader {
  val logger = Logger.getLogger(classOf[ClientLoader])
  @BeanProperty var applet: Applet = null

  /**
   * Loads the game applet based on the information
   * obtained from the client configuration.
   * @param configuration Client configuration
   */
  def start(configuration: ClientConfig) {
    if (!isLoaded) {
      logger.info("Starting new applet")
      val stub = new ClientStub(configuration)
      prepareApplet(configuration)
      applet.setStub(stub)
      applet.init()
      applet.start()
    } else {
      logger.warn("Applet wasn't started, call stop()!")
    }
  }

  /**
   * Stops the applet
   */
  def stop() {
    applet.stop()
    applet.destroy()
    applet = null
    System.gc()
  }

  /**
   * Checks if the applet is loaded and active
   * @return true when loaded and active
   */
  def isLoaded: Boolean = {
    applet != null && applet.isActive
  }

  /**
   * Establishes a JarURLConnection based on information
   * from the client configuration
   * @param config Client configuration
   * @return a jar URL connection
   */
  private def establishJarConnection(config: ClientConfig): JarURLConnection = {
    val docBase = config.getDocumentBase
    val archive = config.getArchiveName
    new URL("jar:" + docBase + "/" + archive + "!/")
      .openConnection().asInstanceOf[JarURLConnection]
  }

  /**
   * Prepares the applet for loading.
   * First it creates a transforming class loader for
   * the client jar, and then it requests the main class and
   * instantiates it. The applet is stored into the applet var
   * @param config Client configuration
   */
  private def prepareApplet(config: ClientConfig) {
    val connection = establishJarConnection(config)
    val loader = new ClientClassLoader(getHooks, connection.getJarFile)
    val mainClass = loader.loadClass(config.getMainClass)
    applet = mainClass.newInstance.asInstanceOf[Applet]
  }

}
