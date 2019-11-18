package com.techsophy.akka

import com.techsophy.akka.db.TestDBConnector
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._

class StudentDetailsTest extends FunSuite with StudentQuery with TestDBConnector {
  test("insert") {
    val insertInput = Await.result(insert(StudentPost("ben", "iit", "bcd", "ben@gmail.com", Some(2))), 10 seconds)
    assert(insertInput === Some(4))
  }
  test("update") {
    val insertInput = Await.result(update(StudentPost("boby", "jntu", "hyd", "bob@gmail.com", Some(1))), 10 seconds)
    assert(insertInput === 1)
  }
  test("get all students") {
    val res = Await.result(getAllStudents(), 10 seconds)
    assert(res.length === 3)
  }
  test("get by name") {
    val insertInput = Await.result(getByName("john"), 10 seconds)
    assert(insertInput === List(StudentPost("john", "iiit", "hyd", "john@gmail.com", Some(3))))
  }
  test("delete") {
    val insertInput = Await.result(delete("ken@gmail.com"), 10 seconds)
    assert(insertInput === 1)
  }
}
