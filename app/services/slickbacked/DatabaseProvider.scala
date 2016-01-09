package services.slickbacked

import slick.driver.JdbcProfile
import slick.backend.DatabaseConfig

import play.api.Configuration
import com.google.inject.{Inject, Singleton}


@Singleton
class DatabaseProvider @Inject() (configuration: Configuration) {

  import services.config._

  private val databaseConfig = DatabaseConfig.forConfig[JdbcProfile] (
    configuration.getMandatoryString("database.config.name"),
    configuration.underlying
  )

  val dataModel = new DataModel(databaseConfig.driver, configuration)
  val db = databaseConfig.db
}