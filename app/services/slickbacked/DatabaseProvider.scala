package services.slickbacked

import slick.driver.JdbcProfile
import slick.backend.DatabaseConfig

import play.api.Configuration
import com.google.inject.{Inject, Singleton}


@Singleton
class DatabaseProvider @Inject() (configuration: Configuration) {

  private val databaseConfig = DatabaseConfig.forConfig[JdbcProfile] (
    configuration
      .getString("database.config.name")
      .getOrElse(sys.error(s"Missing 'database.config.name' Configuration")),
    configuration.underlying
  )

  val dataModel = new DataModel(databaseConfig.driver, configuration)
  val db = databaseConfig.db
}