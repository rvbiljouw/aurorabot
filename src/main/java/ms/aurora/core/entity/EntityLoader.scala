package ms.aurora.core.entity

import java.util
import java.io._
import ms.aurora.api.script.{ScriptManifest, Script}
import ms.aurora.api.plugin.{PluginManifest, Plugin}
import ms.aurora.api.random.{RandomManifest, Random}
import scala.collection.JavaConversions._
import ms.aurora.core.model.Source
import org.apache.log4j.Logger
import java.util.jar.{JarEntry, JarFile}
import scala.collection.JavaConversions.JEnumerationWrapper
import java.net.URLClassLoader
import ms.aurora.api.plugin.internal.{TileUtilities, InterfacePlugin, PaintDebug}

/**
 * Loads classes from database-specified source folders
 * Optionally recursive.
 * @author rvbiljouw
 */
class EntityLoader(recursive: Boolean) {
  val logger = Logger.getLogger(classOf[EntityLoader])
  val randoms = new util.ArrayList[Class[_ <: Random]]
  val scripts = new util.ArrayList[Class[_ <: Script]]
  val plugins = new util.ArrayList[Class[_ <: Plugin]]

  def clear() {
    randoms.clear()
    plugins.clear()
    scripts.clear()
  }

  def load() {
    Source.getAll.foreach(source => {
      logger.info("Scanning source " + source.getSource)
      traverse(new File(source.getSource))
    })

    logger.info("Initializing defaults")
    plugins.add(classOf[PaintDebug])
    plugins.add(classOf[InterfacePlugin])
    plugins.add(classOf[TileUtilities])
  }

  def reload() {
    clear()
    load()
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
    enum.filter(NAME_SUFFIX_FILTER(".class")).foreach(clazzFile => {
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
        case s if classOf[Script].isAssignableFrom(clazz) => scripts.add(clazz.asSubclass(classOf[Script]))
        case r if r.eq(classOf[Random]) => randoms.add(clazz.asSubclass(classOf[Random]))
        case p if p.eq(classOf[Plugin]) => plugins.add(clazz.asSubclass(classOf[Plugin]))
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
    name.replaceAllLiterally("/", ".").dropRight(6)

  private val NAME_SUFFIX_FILTER =
    (suffix: String) => (entry: JarEntry) => entry.getName.endsWith(suffix)

}

object EntityLoader {

  private val instance = new EntityLoader(true)

  def get = instance

}