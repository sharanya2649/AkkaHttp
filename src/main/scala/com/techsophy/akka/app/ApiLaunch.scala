package com.techsophy.akka.app

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.techsophy.akka.ApiObj

import scala.concurrent.ExecutionContext

object ApiLaunch extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val bindingFuture = Http().bindAndHandle(ApiObj.routes, "localhost", 8085)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
}

