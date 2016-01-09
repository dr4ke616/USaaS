package mixin

import play.api.Application
import services.slickbacked.DatabaseProvider

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global


trait ProvidedDatabase {

  def app: Application

  val databaseProvider = new DatabaseProvider(app.configuration)
  val dm = databaseProvider.dataModel

  import dm.driver.api._

  def cleanSchema() = {
    val completion = for {
      _ <- databaseProvider.db.run(DBIO.seq(dm.schema.drop)).recover { case ex => () }
      _ <- databaseProvider.db.run(DBIO.seq(dm.schema.create))
    } yield ()
    Await.result(completion, 10.seconds)
  }
}