package ms.aurora.core.entity

import java.io._
import java.net.URLClassLoader
import java.util
import java.util.jar.{JarEntry, JarFile}
import ms.aurora.api.plugin.internal.{TileUtilities, InterfacePlugin, PaintDebug}
import ms.aurora.api.plugin.{PluginManifest, Plugin}
import ms.aurora.api.random.{RandomManifest, Random}
import ms.aurora.api.script.{ScriptManifest, Script}
import ms.aurora.core.model.Source
import org.apache.log4j.Logger
import scala.beans.BeanProperty
import scala.collection.JavaConversions._

/**
 * Loads classes from database-specified source folders
 * Optionally recursive.
 * @author rvbiljouw
 */
class EntityLoader(recursive: Boolean) {
  val logger = Logger.getLogger(classOf[EntityLoader])
  @BeanProperty val randoms = new util.ArrayList[Random]
  @BeanProperty val scripts = new util.ArrayList[Script]
  @BeanProperty val plugins = new util.ArrayList[Plugin]

  def clear() {
    randoms.clear()
    plugins.clear()
    scripts.clear()
  }

  def load() {
    clear()

    Source.getAll.foreach(source => {
      logger.info("Scanning source " + source.getSource)
      traverse(new File(source.getSource))
    })

    logger.info("Initializing defaults")
    plugins.add(new PaintDebug())
    plugins.add(new TileUtilities())
    plugins.add(new InterfacePlugin())
  }

  private def traverse(root: File) {
    val files = root.listFiles
    files.foreach(file => {
      file.isDirectory match {
        case true => if (recursive) traverse(file)
        case false => loadJar(file)
      }
    })
  }

  private def loadJar(rawFile: File) {
    val file = new JarFile(rawFile)
    val loader = new URLClassLoader(Array(rawFile.toURI.toURL))
    val enum = new JEnumerationWrapper(file.entries)
    enum.filter(NAME_SUFFIX_FILTER(".class")).foreach(clazzFile => {
      val strip = formatClassName(clazzFile.getName)
      loadClass(loader.loadClass(strip))
    })
  }

  private def loadClass(clazz: Class[_]) {
    if (!hasAnnotation(clazz)) {
      logger.info("Malformed class: " + clazz.getName)
    } else {
      clazz.getSuperclass match {
        case s if s.eq(classOf[Script]) => {
          val i = clazz.newInstance
          if (i != null) {
            scripts.add(i.asInstanceOf[Script])
          }
        }
        case r if r.eq(classOf[Random]) => {
          val i = clazz.newInstance
          if (i != null) {
            randoms.add(i.asInstanceOf[Random])
          }
        }
        case p if p.eq(classOf[Plugin]) => {
          val i = clazz.newInstance
          if (i != null) {
            plugins.add(i.asInstanceOf[Plugin])
          }
        }
        case _ => logger.info("Unknown superclass in " + clazz.getName)
      }
    }
  }

  private def hasAnnotation(clazz: Class[_]): Boolean = {
    clazz.getAnnotation(classOf[ScriptManifest]) != null ||
      clazz.getAnnotation(classOf[RandomManifest]) != null ||
      clazz.getAnnotation(classOf[PluginManifest]) != null
  }

  private def formatClassName(name: String): String =
    name.replaceAllLiterally("/", ".").replace(".class", "")

  private val NAME_SUFFIX_FILTER =
    (suffix: String) => (entry: JarEntry) => entry.getName.endsWith(suffix)
}
