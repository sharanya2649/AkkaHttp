package com.techsophy.akka

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.techsophy.akka.oauth2.{KeycloakTokenVerifier, OAuth2Authorization}
import org.keycloak.adapters.KeycloakDeploymentBuilder

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Api extends Json {
  val oauth2 = new OAuth2Authorization(
    new KeycloakTokenVerifier(
      "05dd171d-ad5a-4033-be0e-bde49e29ee06",
      KeycloakDeploymentBuilder.build(getClass.getResourceAsStream("/keycloak.json")),
    )
  )

  import oauth2._

  val studentObj: StudentQuery
  val routes: Route = {
    path("student" / "user") {
      get {
        parameters('name.as[String]) {
          username =>
            val userToken = studentObj.checkUser(username)
            complete(userToken)
        }
      }
    } ~
      path("student" / "create") {
        authorized { token =>

          post {
            entity(as[StudentPost]) {
              json =>
                val saved = studentObj.insert(json)
                onComplete(saved) { done =>
                  complete("inserted")
                }
            }
          }
        }
      } ~
      path("student" / "getAllStudents") {
        authorized { token =>
          get {
            val data: Future[List[StudentPost]] = studentObj.getAllStudents()
            onComplete(data) { done =>
              complete(data)
            }
          }
        }
      } ~
      path("student" / "update") {
        authorized { token =>
          post {
            entity(as[StudentPost]) {
              json =>
                val update = studentObj.update(json)
                onComplete(update) { done =>
                  complete("updated")
                }
            }
          }
        }
      } ~
      path("student" / "delete") {
        authorized { token =>
          post {
            entity(as[String]) {
              email =>
                val delete = studentObj.delete(email)
                onComplete(delete) { done =>
                  complete("deleted")
                }
            }
          }
        }
      } ~
      path("student" / "getByName") {
        authorized { token =>
          get {
            parameters('name.as[String]) {
              name =>
                val getName = studentObj.getByName(name)
                onComplete(getName) { done =>
                  complete {
                    getName.map(x => x)
                  }
                }
            }
          }
        }
      }
  }


}

object ApiObj extends Api {
  override val studentObj: StudentQuery = StudentQueryObj
}