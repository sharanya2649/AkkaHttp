package com.techsophy.akka


import com.techsophy.akka.db.{DBConnection, MySQLDBConnector}

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
