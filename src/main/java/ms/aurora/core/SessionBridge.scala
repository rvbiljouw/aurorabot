package ms.aurora.core

import akka.actor.Actor
import ms.aurora.core.script.{TickEvent, TickResult}

import scala.concurrent.duration._
import org.apache.log4j.Logger
/**
 * @author rvbiljouw
 */
class SessionBridge(session: Session) extends Actor {
  private val logger = Logger.getLogger(classOf[SessionBridge])
  var lastTick: Long = 0
  var lastDelay: Long = 0

  override def preStart() {
    val scheduler = session.getActorSystem.scheduler
    scheduler.schedule(0 milliseconds, 250 milliseconds,
      self, Heartbeat())(scala.concurrent.ExecutionContext.global)
  }

  def receive: Actor.Receive = {
    case TickResult(time) => lastDelay = time
    case Heartbeat() => heartbeat()
  }

  def heartbeat() {
    if ((System.currentTimeMillis() - lastTick) >= lastDelay) {
      session.getScriptSupervisor ! TickEvent()
      lastTick = System.currentTimeMillis()
    }
  }

}

case class Heartbeat()
