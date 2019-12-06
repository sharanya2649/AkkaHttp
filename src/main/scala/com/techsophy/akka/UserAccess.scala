package com.techsophy.akka

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.admin.client.{Keycloak, KeycloakBuilder}


class UserAccess {
  def verifyUser(userId: String): Boolean = {

    val SERVER_URL = "http://localhost:8080/auth"
    val REALM = "demo"
    val USERNAME = "john"
    val PASSWORD = "admin"
    val CLIENT_ID = "vanilla"
    try {
      val keycloak: Keycloak = KeycloakBuilder
        .builder()
        .serverUrl(SERVER_URL)
        .realm(REALM)
        .username(USERNAME)
        .password(PASSWORD)
        .clientId(CLIENT_ID)
        .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
        .build()

      val users = keycloak.realm("demo").users()
      val id: UserResource = users.get(userId)
      val username = id.toRepresentation.getUsername
      true
    }
    catch {
      case ex: Exception =>
        false
    }


  }
}
