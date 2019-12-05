package com.techsophy.akka.oauth2

import java.util.concurrent.ForkJoinPool

import com.techsophy.akka.UserAccess
import org.keycloak.RSATokenVerifier
import org.keycloak.adapters.KeycloakDeployment

import scala.concurrent.{ExecutionContext, Future}

class KeycloakTokenVerifier(kId: String, keycloakDeployment: KeycloakDeployment) extends UserAccess {
  implicit val executionContext = ExecutionContext.fromExecutor(new ForkJoinPool(2))

  def verifyToken(token: String): Future[String] = Future {
    val userId = RSATokenVerifier.verifyToken(
      token,
      keycloakDeployment.getPublicKeyLocator
        .getPublicKey(kId, keycloakDeployment),
      keycloakDeployment.getRealmInfoUrl, true, false
    ).getSubject
    if (verifyUser(userId)) {
      userId
    }
    else {
      "unauthorized"
    }

  }
}

