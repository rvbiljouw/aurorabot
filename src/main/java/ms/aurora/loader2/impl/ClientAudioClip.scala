package ms.aurora.loader2.impl

import org.apache.log4j.Logger
import java.net.URL
import java.applet.AudioClip

/**
 * Client audio clip implementation.
 * This is only here for completeness sake,
 * I have no idea if it's actually used but I put
 * some debugs in here to make sure if its.
 * @author rvbiljouw
 */
class ClientAudioClip(url: URL) extends AudioClip {
  private val logger = Logger.getLogger(classOf[ClientAudioClip])
  private val currentClip = loadClip

  def play() {
    logger.info("Playing of audio clip requested")
    currentClip.play()
  }

  def loop() {
    logger.info("Looping of audio clip requested")
    currentClip.stop()
    currentClip.setCycleCount(javafx.scene.media.AudioClip.INDEFINITE)
    currentClip.play()
  }

  def stop() {
    logger.info("Stopping of audio clip requested")
    currentClip.stop()
  }

  private def loadClip: javafx.scene.media.AudioClip = {
    new javafx.scene.media.AudioClip(url.toString)
  }

}
