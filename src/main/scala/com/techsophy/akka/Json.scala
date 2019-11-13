package com.techsophy.akka

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait Json extends SprayJsonSupport with DefaultJsonProtocol{
  implicit val studentPost: RootJsonFormat[StudentPost] = jsonFormat5(StudentPost)
  implicit val studentPosts: RootJsonFormat[StudentPosts] = jsonFormat1(StudentPosts)

}
