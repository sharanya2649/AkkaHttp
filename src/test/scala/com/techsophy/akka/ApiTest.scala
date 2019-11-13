package com.techsophy.akka

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class ApiTest extends WordSpec with Matchers with ScalatestRouteTest {
  "The service" should {
    "return a 'inserted' response for GET requests to /create" in {
      // tests:
//      val requestJson: String = """{"name":"ken","cname":"mit","location":"us","email":"ken@gmail.com"}"""
//      Post("/student/create", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> ApiObj.routes ~> check {
//        responseAs[String] shouldEqual "inserted"
//      }
//      Get("/student/getAllStudents")~> ApiObj.routes ~> check {
//        responseAs[String] shouldEqual """[{"cname":"IIIT","email":"bob@gmail.com","id":1,"location":"hyd","name":"boby"},{"cname":"NIT","email":"peter@gmail.com","id":2,"location":"warangal","name":"peter"},{"cname":"IIT","email":"john@gmail.com","id":3,"location":"bangalore","name":"john"}]"""
//      }
//      val updateJson= """{"name":"boby","cname":"jntuk","location":"kakinada","email":"bob@gmail.com","id":1}"""
//      Post("/student/update",HttpEntity(ContentTypes.`application/json`, updateJson))~> ApiObj.routes ~> check {
//        responseAs[String] shouldEqual "updated"
//      }
//      val deleteJson= """ken@gmail.com"""
//      Post("/student/delete",HttpEntity(ContentTypes.`application/json`, deleteJson))~> ApiObj.routes ~> check {
//        responseAs[String] shouldEqual "deleted"
//      }
      Get("/student/getByName?name=john")~> ApiObj.routes ~> check {
        responseAs[String] shouldEqual """[{"cname":"IIT","email":"john@gmail.com","id":3,"location":"bangalore","name":"john"}]"""
      }

    }
  }
}
