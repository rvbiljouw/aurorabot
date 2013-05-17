package ms.aurora.api

import akka.actor.Actor

/**
 * @author rvbiljouw
 */
abstract class Random2 extends Context with Actor {

  def activate(): Boolean

  def loop(): Int

}
