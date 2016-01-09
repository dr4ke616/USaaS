package services

import java.util

import play.api.Configuration


package object config {
  implicit class ConfigOps(val config: Configuration) extends AnyVal {
    def getMandatoryConfig(name: String): Configuration = config.getConfig(name).getOrElse(sys.error(s"Missing '$name' Configuration"))
    def getMandatoryString(name: String): String = config.getString(name).getOrElse(sys.error(s"Missing '$name' String"))
    def getMandatoryList(name: String): util.List[String] = config.getStringList(name).getOrElse(sys.error(s"Missing '$name' List"))
    def getMandatoryBoolean(name: String): Boolean = config.getBoolean(name).getOrElse(sys.error(s"Missing '$name' Boolean"))
    def getMandatoryInt(name: String): Int = config.getInt(name).getOrElse(sys.error(s"Missing '$name' Int"))
    def getMandatoryDouble(name: String): Double = config.getDouble(name).getOrElse(sys.error(s"Missing '$name' Double"))
    def asMap(): java.util.Map[String, AnyRef] = config.underlying.root().unwrapped()
  }
}