package ms.aurora.api.random

import akka.actor.Actor
import ms.aurora.api.Context
import org.apache.log4j.Logger

/**
 * Enum for random states
 */
object RandomResult extends scala.Enumeration {
  type RandomResult = Value
  val Success, Error, Idle = Value
}

import RandomResult._

/**
 * Every random is an actor.
 * Randoms are asynchronous
 * @author rvbiljouw
 */
abstract class Random2 extends Context with Actor {
  private val logger = Logger.getLogger(classOf[Random2])

  def activate(): Boolean

  def loop(): Int

  def info(message: String) {
    logger.info(message)
  }

  def error(message: String) {
    logger.error(message)
  }

  override def receive = {
    case Tick() => sender ! TickResult(run())
  }

  def run(): RandomResult = {
    if(activate()) {
      val status = loop()
      status match {
        case Random2.ERROR => Error
        case Random2.SUCCESS => Success
        case _ => {
          Thread.sleep(status)
          run()
        }
      }
    } else Idle
  }

  case class Tick()

  case class TickResult(randomResult: RandomResult)

}

object Random2 {
  val ERROR = -1
  val SUCCESS = -2
}