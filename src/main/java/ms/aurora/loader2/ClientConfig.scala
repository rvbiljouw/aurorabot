package ms.aurora.loader2

import scala.collection.mutable
import ms.aurora.browser.{Browser, ResponseHandler}
import java.io.InputStream
import scala.beans.BeanProperty
import ms.aurora.browser.impl.GetRequest
import ms.aurora.browser.wrapper.Plaintext

/**
 * Client configuration file
 * @author rvbiljouw
 */
class ClientConfig extends ResponseHandler {
  private val params = new mutable.HashMap[String, String]
  @BeanProperty var documentBase: String = null
  @BeanProperty var archiveName: String = null
  @BeanProperty var mainClass: String = null

  def getParam(key: String) = params(key)

  def visit(browser: Browser) {
    browser.doRequest(formulateRequest, this)
  }

  def handleResponse(inputStream: InputStream) {
    val plaintext = Plaintext.fromStream(inputStream)
    plaintext.setText(plaintext.getText.replaceAll("param=", "").replaceAll("msg=", ""))
    val lines: Array[String] = plaintext.getText.split("\n")
    for (line <- lines) {
      params += extractKVPair(line)
    }

    documentBase = getParam("codebase")
    archiveName = getParam("initial_jar")
    mainClass = getParam("initial_class").replace(".class", "")
  }

  private def extractKVPair(string: String): (String, String) = {
    val len = string.length
    val idx = string.indexOf('=')
    (string.substring(0,idx) -> string.substring(idx + 1, len))
  }

  private def formulateRequest: GetRequest = {
    val request = new GetRequest()
    request.setPage(ClientConfig.CONFIG_URL)
    request
  }
}

object ClientConfig {

  val CONFIG_URL = "/jav_config.ws"

}
