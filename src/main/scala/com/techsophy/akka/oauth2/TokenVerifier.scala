package com.techsophy.akka.oauth2

import org.keycloak.representations.AccessToken

import scala.concurrent.Future

trait TokenVerifier {
  def verifyToken(token: String): Future[AccessToken]
}
