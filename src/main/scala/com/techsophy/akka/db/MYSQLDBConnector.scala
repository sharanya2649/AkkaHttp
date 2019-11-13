package com.techsophy.akka.db

import com.typesafe.config.ConfigFactory
import slick.jdbc.MySQLProfile

trait MySQLDBConnector extends DBConnection {

  val driver = MySQLProfile

  import driver.api._

  def db: MySQLProfile.backend.DatabaseDef = Database.forConfig("db",ConfigFactory.load().withFallback(ConfigFactory.load("application")))
}
