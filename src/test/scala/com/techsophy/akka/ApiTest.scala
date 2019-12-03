package com.techsophy.akka

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.AuthorizationFailedRejection
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.softwaremill.sttp._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class ApiTest extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar {

  val mockedDetails = mock[StudentQuery]

  "The service" should {
    implicit val backend = HttpURLConnectionBackend()
    implicit val formats = DefaultFormats

    val response: Id[Response[Map[String, String]]] = sttp.post(uri"http://localhost:8080/auth/realms/demo/protocol/openid-connect/token")
      .headers(Map("Content-Type" -> "application/x-www-form-urlencoded"))
      .body(
        "grant_type" -> "password",
        "username" -> "john",
        "password" -> "admin",
        "client_id" -> "vanilla"
      ).mapResponse(re => {
      parse(re.toString).mapField {
        case (key, JString(value)) => (key, JString(value.toString))
        case x => x
      }.extract[Map[String, String]]
    })
      .send()
    println(response.body + "----------")
    val accessToken: String = response.body.right.get("access_token")
    println(accessToken + "......")

    "insert" in {
      // tests:
      val requestJson: String = """{"name":"ken","cname":"mit","location":"us","email":"ken@gmail.com"}"""
      when(mockedDetails.insert(StudentPost("ken2", "nit", "us", "ken2@gmail.com"))).thenReturn(Future(Some(1)))
      Post("/student/create", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> addHeader("Authorization", s"Bearer $accessToken") ~> ApiObj.routes ~> check {
        responseAs[String] shouldEqual "inserted"
      }
    }
    "delete" in {
      val deleteJson = """ken@gmail.com"""
      when(mockedDetails.delete("ken@gmail.com")).thenReturn(Future(1))
      Post("/student/delete", HttpEntity(ContentTypes.`application/json`, deleteJson)) ~> addHeader("Authorization", s"Bearer $accessToken") ~> ApiObj.routes ~> check {
        responseAs[String] shouldEqual "deleted"
      }
    }
    "getAllStudents" in {
      Get("/student/getAllStudents") ~> addHeader("Authorization", s"Bearer $accessToken") ~> ApiObj.routes ~> check {
        when(mockedDetails.getAllStudents()).thenReturn(Future(List(StudentPost("boby", "jntuk", "kakinada", "bob@gmail.com", Some(1)), StudentPost("boby", "jntuk", "kakinada", "bob@gmail.com", Some(1)), StudentPost("boby", "jntuk", "kakinada", "bob@gmail.com", Some(1)))))
        responseAs[String] shouldEqual """[{"cname":"jntu","email":"bob@gmail.com","id":1,"location":"kakinada","name":"boby"},{"cname":"NIT","email":"peter@gmail.com","id":2,"location":"warangal","name":"peter"},{"cname":"IIT","email":"john@gmail.com","id":3,"location":"bangalore","name":"john"}]"""
      }
    }
    "update" in {
      val updateJson = """{"name":"boby","cname":"jntu","location":"kakinada","email":"bob@gmail.com","id":1}"""
      when(mockedDetails.update(StudentPost("boby", "jntu", "hyd", "bob@gmail.com", Some(1)))).thenReturn(Future(1))
      Post("/student/update", HttpEntity(ContentTypes.`application/json`, updateJson)) ~> addHeader("Authorization", s"Bearer $accessToken") ~> ApiObj.routes ~> check {
        responseAs[String] shouldEqual "updated"
      }
    }
    "getByName" in {
      Get("/student/getByName?name=john") ~> addHeader("Authorization", s"Bearer $accessToken") ~> ApiObj.routes ~> check {
        when(mockedDetails.getByName("john")).thenReturn(Future(List(StudentPost("johnn", "IIT", "bangalore", "john@gmail.com", Some(2)))))
        responseAs[String] shouldEqual """[{"cname":"IIT","email":"john@gmail.com","id":3,"location":"bangalore","name":"john"}]"""
      }
    }


  }
  "unauthenticated request" should {
    "no access" in {
      Get("/student/getByName?name=john") ~> ApiObj.routes ~> check {
        rejection shouldEqual AuthorizationFailedRejection
      }
    }
  }
}
