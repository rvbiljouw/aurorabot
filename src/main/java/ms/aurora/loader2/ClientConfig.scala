package ms.aurora.loader2

import scala.collection.mutable

/**
 * Client configuration file
 * @author rvbiljouw
 */
class ClientConfig(url: String) {
  private val params = new mutable.HashMap[String, String]

  def getParam(key: String) = params(key)


}
