package com.techsophy.akka

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext.Implicits.global

trait Api extends Json {
  val studentObj: StudentQuery
  val routes: Route = {
    path("student" / "create") {
      post {
        entity(as[StudentPost]) {
          json =>
            val saved = studentObj.insert(json)
            onComplete(saved) { done =>
              complete("inserted")
            }
        }
      }
    } ~
      path("student" / "getAllStudents") {
        get {
          val data = studentObj.getAllStudents()
          onComplete(data) { done =>
            complete(data)
          }
        }
      } ~
      path("student" / "update") {
        post {
          entity(as[StudentPost]) {
            json =>
              val update = studentObj.update(json)
              onComplete(update) { done =>
                complete("updated")
              }
          }
        }
      } ~
      path("student" / "delete") {
        post {
          entity(as[String]) {
            email =>
              val delete = studentObj.delete(email)
              onComplete(delete) { done =>
                complete("deleted")
              }
          }
        }
      } ~
      path("student" / "getByName") {
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

object ApiObj extends Api {
  override val studentObj: StudentQuery = StudentQueryObj
}