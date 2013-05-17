package ms.aurora.core.actor

import akka.actor.Actor
import ms.aurora.core.Session
import ms.aurora.api.random.Random
import ms.aurora.core.entity.EntityLoader.get
import scala.collection.JavaConversions._
import org.apache.log4j.Logger

/**
 * Actor responsible for dealing with random events
 * @author rvbiljouw
 */
class RandomSupervisor(parent: Session) extends Actor {
  val logger = Logger.getLogger(classOf[RandomSupervisor])
  val randoms = get.randoms.map(_.newInstance)

  def receive = {
    case TickEvent() => tick()
  }

  private def tick() {
    randoms.foreach(invoke(_))
  }

  private def invoke(random: Random) {
    random.setSession(parent)
    while (random.activate()) {
      val delay = random.loop()
      if (delay != -1) {
        Thread.sleep(delay)
      } else return
    }
  }
}

case class TickEvent()
