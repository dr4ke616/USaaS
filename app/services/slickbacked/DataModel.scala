package services.slickbacked

import play.api.Configuration
import slick.driver.JdbcProfile


class DataModel(val driver: JdbcProfile, configuration: Configuration) {

  import driver.api._

  case class UrlHashMap(id: Long = 0L, original_url: String, hash: String)

  class UrlHashMapTable(tag: Tag) extends Table[UrlHashMap](tag, "url_hash_map") {
    def id = column[Long]("id", O.PrimaryKey)
    def originalUrl = column[String]("original_url")
    def hash = column[String]("hash")
    def * = (id, originalUrl, hash) <> (UrlHashMap.tupled, UrlHashMap.unapply _)
  }

  val urlHashMap = TableQuery[UrlHashMapTable]
  lazy val schema = Seq(urlHashMap).map(_.schema).reduce(_ ++ _)
}
