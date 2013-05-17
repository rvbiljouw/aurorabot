package ms.aurora.core

import scala.collection.mutable
import java.lang.Thread.currentThread


/**
 * A class for holding Session instances
 * @author rvbiljouw
 */
object Repository {
  private val instancesPerGroup = new mutable.HashMap[ThreadGroup, Session]
  private val instancesPerHash =  new mutable.HashMap[Int, Session]

  def get(hashCode: Int): Session = instancesPerHash(hashCode)

  def get(threadGroup: ThreadGroup): Session = instancesPerGroup(threadGroup)

  def set(group: ThreadGroup, instance: Session) = instancesPerGroup += (group -> instance)

  def set(hashCode: Int, instance: Session) {
    val group = currentThread().getThreadGroup
    instancesPerHash += (hashCode -> instance)
    instancesPerGroup += (group -> instance)
  }

  def all(): Iterable[Session] = instancesPerHash.values

}
