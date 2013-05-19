package ms.aurora.core.entity

import java.util
import java.io._
import ms.aurora.api.script.{ScriptManifest, Script}
import ms.aurora.api.plugin.{PluginManifest, Plugin}
import ms.aurora.api.random.{RandomManifest, Random}
import scala.collection.JavaConversions._
import ms.aurora.core.model.Source
import org.apache.log4j.Logger
import java.util.jar.JarFile
import scala.collection.JavaConversions.JEnumerationWrapper
import java.net.URLClassLoader
import ms.aurora.api.plugin.internal.{InterfacePlugin, TileUtilities, PaintDebug}
import scala.beans.BeanProperty

/**
 * Loads classes from database-specified source folders
 * Optionally recursive.
 * @author rvbiljouw
 */
class EntityLoader(recursive: Boolean) {
  val logger = Logger.getLogger(classOf[EntityLoader])
  val randoms = new util.ArrayList[Random]
  val scripts = new util.ArrayList[Script]
  val plugins = new util.ArrayList[Plugin]

  def clear() {
    randoms.clear()
    plugins.clear()
    scripts.clear()

    plugins.add(new PaintDebug())
    plugins.add(new TileUtilities())
    plugins.add(new InterfacePlugin())
  }

  def load() {
    Source.getAll.foreach(source => {
      logger.info("Scanning source " + source.getSource)
      traverse(new File(source.getSource))
    })
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
    val loader = new URLClassLoader(
      Array(rawFile.toURI.toURL))

    val enum = new JEnumerationWrapper(file.entries)
    enum.foreach(clazzFile => {
      val name = clazzFile.getName
      if (name.endsWith(".class")) {
        val strip = formatClassName(name)
        loadClass(loader.loadClass(strip))
      }
    })
  }

  private def loadClass(clazz: Class[_]) {
    if (!hasAnnotation(clazz)) {
      logger.info("Malformed class: " + clazz.getName)
    } else {
      clazz.getSuperclass match {
        case s if classOf[Script].isAssignableFrom(clazz) => {
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
    name.replaceAll("/", "\\.").replace(".class", "")

  def getPlugins = plugins

  def getScripts = scripts

  def getRandoms = randoms

}

object EntityLoader {
  private val instance = new EntityLoader(true)

  def get: EntityLoader = {
    instance.clear()
    instance.load()
    instance
  }

}