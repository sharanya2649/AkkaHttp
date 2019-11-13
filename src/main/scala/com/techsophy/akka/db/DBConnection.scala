package com.techsophy.akka.db

import slick.jdbc.JdbcProfile

trait DBConnection {

  val driver: JdbcProfile

  def db: driver.backend.DatabaseDef

}
