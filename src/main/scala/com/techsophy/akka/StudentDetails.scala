package com.techsophy.akka


import com.softwaremill.sttp.{HttpURLConnectionBackend, Id, Response, sttp, _}
import com.techsophy.akka.db.{DBConnection, MySQLDBConnector}
import org.json4s.native.JsonMethods.parse
import org.json4s.{DefaultFormats, JString}

trait StudentQuery extends StudentDetails {

  import driver.api._

  def insert(studentPost: StudentPost) = {
    db.run(studentInc += studentPost)
  }

  def update(studentPost: StudentPost) = {
    db.run(student.filter(_.email === studentPost.email).update(studentPost))
  }

  def delete(email: String) = {
    db.run(student.filter(_.email === email).delete)
  }

  def getAllStudents() = {
    db.run(student.to[List].result)
  }

  def getByName(name: String) = {
    db.run(student.filter(_.name === name).to[List].result)
  }

  def checkUser(user: String) = {
    try {
      implicit val backend = HttpURLConnectionBackend()
      implicit val formats = DefaultFormats

      val response: Id[Response[Map[String, String]]] = sttp.post(uri"http://localhost:8080/auth/realms/demo/protocol/openid-connect/token")
        .headers(Map("Content-Type" -> "application/x-www-form-urlencoded"))
        .body(
          "grant_type" -> "password",
          "username" -> user,
          "password" -> "admin",
          "client_id" -> "vanilla"
        ).mapResponse(re => {
        parse(re.toString).mapField {
          case (key, JString(value)) =>
            val res = (key, JString(value.toString))
            res
          case x =>
            x
        }.extract[Map[String, String]]
      })
        .send()
      val accessToken: String = response.body.right.get("access_token")
      accessToken
    }
    catch {
      case ex: Exception =>
        "Exception ....."
    }
  }

  case class StudentOrder(items: List[StudentPost])

}

trait StudentDetails extends DBConnection {

  import driver.api._

  val student = TableQuery[StudentTable]

  def studentInc = student returning student.map(_.id)


  class StudentTable(tag: Tag) extends Table[StudentPost](tag, "student_table") {
    def * = (name, cname, location, email, id) <> (StudentPost.tupled, StudentPost.unapply)

    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def cname = column[String]("cname")

    def location = column[String]("location")

    def email = column[String]("email")

  }

}

final case class StudentPost(name: String, cname: String, location: String, email: String, id: Option[Int] = None)

final case class StudentPosts(students: StudentPost)

object StudentQueryObj extends StudentQuery with MySQLDBConnector
