package ms.aurora.core.script

/**
 * Enum for state transition responses
 */
object ScriptResult extends scala.Enumeration {
  type ScriptResult = Value
  val Started, Paused, Resumed, Stopped, Failed = Value
}

import ScriptResult._
import akka.actor.Actor
import akka.actor.ActorLogging
import ms.aurora.api.script.Script
import scala.collection.mutable
import ms.aurora.api.script.ScriptState._
import ms.aurora.api.script.ScriptState
import ms.aurora.core.Session

/**
 * An Akka supervisor for all script tasks.
 * @author rvbiljouw
 */
class ScriptSupervisor(parent: Session) extends Actor with ActorLogging {
  var active = new mutable.HashMap[Class[_ <: Script], Script]
  var running = false

  def receive = {
    case StartEvent(clazz) => sender ! startScript(clazz)
    case PauseEvent(clazz) => sender ! pauseScript(clazz)
    case ResumeEvent(clazz) => sender ! resumeScript(clazz)
    case StopEvent(clazz) => sender ! stopScript(clazz)
    case TickEvent() => sender ! tick()
      parent.active = isActive
  }

  /**
   * Starts a script
   * @param scriptClass Class of script to start
   * @return State transition
   */
  def startScript(scriptClass: Class[_ <: Script]): StateTransition = {
    if (active.contains(scriptClass)) {
      return StateTransition(Failed, "Only one instance can" +
        " be active at the same time")
    }

    try {
      val instance = scriptClass.newInstance
      instance.init()
      instance.onStart()
      instance.setState(RUNNING)
      StateTransition(Started)
    } catch {
      case e: Exception => StateTransition(Failed,
        "Failed to start -> " + e.toString)
    }
  }

  /**
   * Pauses a script
   * @param scriptClass Class of script to pause
   * @return State transition
   */
  def pauseScript(scriptClass: Class[_ <: Script]): StateTransition = {
    if (scriptClass == null) {
      active.foreach(s => pauseScript(s._1))
      StateTransition(Paused)
    } else if (!active.contains(scriptClass)) {
      StateTransition(Failed, "Non-running scripts " +
        "cannot be paused")
    } else {
      val instance = active(scriptClass)
      instance.setState(PAUSED)
      StateTransition(Paused)
    }
  }

  /**
   * Resumes a script
   * @param scriptClass Class of script to resume
   * @return State transition
   */
  def resumeScript(scriptClass: Class[_ <: Script]): StateTransition = {
    if (scriptClass == null) {
      active.foreach(s => resumeScript(s._1))
      StateTransition(Resumed)
    } else if (!active.contains(scriptClass)) {
      StateTransition(Failed, "Non-running scripts " +
        "cannot be resumed")
    } else {

      val instance = active(scriptClass)
      instance.setState(RUNNING)
      StateTransition(Resumed)
    }
  }

  /**
   * Stops a script
   * @param scriptClass Class of script to stop
   * @return State transition
   */
  def stopScript(scriptClass: Class[_ <: Script]): StateTransition = {
    if (scriptClass == null) {
      active.foreach(s => stopScript(s._1))
      StateTransition(Resumed)
    } else if (!active.contains(scriptClass)) {
      StateTransition(Failed, "Non-running scripts " +
        "cannot be resumed")
    } else {
      val instance = active(scriptClass)
      instance.cleanup()
      active -= scriptClass
      StateTransition(Stopped)
    }
  }

  /**
   * When called, this method calls tick()
   * on all scripts under this supervisor.
   *
   * TODO: Exception handling
   * @return A tick result, specifying the wait until the next
   *         tick should placed.
   */
  def tick(): TickResult = {
    var timeUntilNextRun = 100 // Base time
    active.foreach(entry =>
      timeUntilNextRun += entry._2.tick())
    TickResult(timeUntilNextRun)
  }

  def isActive: Boolean = {
    active.count(_._2.getState == ScriptState.RUNNING) > 0
  }

}

case class StateTransition(state: ScriptResult, reason: String = "")

case class StartEvent(scriptClass: Class[_ <: Script])

case class PauseEvent(scriptClass: Class[_ <: Script])

case class StopEvent(scriptClass: Class[_ <: Script])

case class ResumeEvent(ScriptClass: Class[_ <: Script])

case class TickResult(until: Int)

case class TickEvent()
