package ms.aurora.core.actor

import akka.actor.Actor

import scala.concurrent.duration._
import org.apache.log4j.Logger
import ms.aurora.core.Session

/**
 * An actor responsible for managing the extensions of a Session
 * Things like script ticks and randoms are handled here.
 * The default interval for processing is 250ms.
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
    case ScriptEvent.TickResult(time) => lastDelay = time
    case Heartbeat() => heartbeat()
  }

  def heartbeat() {
    if ((System.currentTimeMillis() - lastTick) >= lastDelay) {
      session.getScriptSupervisor ! ScriptEvent.Tick()
      lastTick = System.currentTimeMillis()
    }
  }
}

case class Heartbeat()
