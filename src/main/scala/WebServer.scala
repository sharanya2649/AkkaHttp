import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn
import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.util.Random
import scala.io.StdIn
import scala.util.Success
import scala.util.Failure
object WebServer {

    implicit val system: ActorSystem = ActorSystem()
    implicit val executor: ExecutionContext = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    def route = path("hello") {
        get {
            complete("Hello, World!")
        }
    }

    val responseFuture:
        Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://akka.io"))

    responseFuture
      .onComplete {
          case Success(res) => println(res)
          case Failure(_)   => sys.error("something wrong")
      }

//    val requestHandler: HttpRequest => HttpResponse = {
//        case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
//            HttpResponse(entity = HttpEntity(
//                ContentTypes.`text/html(UTF-8)`,
//                "<html><body>Hello world!</body></html>"))
//
//        case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
//            HttpResponse(entity = "PONG!")
//
//        case HttpRequest(GET, Uri.Path("/crash"), _, _, _) =>
//            sys.error("BOOM!")
//
//        case r: HttpRequest =>
//            r.discardEntityBytes() // important to drain incoming HTTP Entity stream
//            HttpResponse(404, entity = "Unknown resource!")
//    }
    val numbers = Source.fromIterator(() =>
        Iterator.continually(Random.nextInt()))

    val route2 =
        path("random") {
            get {
                complete(
                    HttpEntity(
                        ContentTypes.`text/plain(UTF-8)`,
                        // transform each number to a chunk of bytes
                        numbers.map(n => ByteString(s"$n\n"))
                    )
                )
            }
        }

    //    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
//    val bindingFuture = Http().bindAndHandleSync(requestHandler, "localhost", 8080)
val bindingFuture = Http().bindAndHandle(route2, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

}
