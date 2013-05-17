package ms.aurora.core

import scala.collection.mutable


/**
 * A class for holding Session instances
 * @author rvbiljouw
 */
object Repository {
  private val instancesPerGroup = new mutable.HashMap[ThreadGroup, Session]
  private val instancesPerHash =  new mutable.HashMap[Int, Session]

  def get(hashCode: Int): Session = instancesPerHash(hashCode)

  def get(threadGroup: ThreadGroup): Session = instancesPerGroup(threadGroup)

  def set(hashCode: Int, instance: Session) { instancesPerHash += (hashCode -> instance) }

  def set(group: ThreadGroup, instance: Session) { instancesPerGroup += (group -> instance) }

}
