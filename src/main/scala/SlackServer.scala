import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContext

class SlackServer(implicit s: ActorSystem, m: ActorMaterializer, ec: ExecutionContext) extends SlackService {
  def startServer(address: String, port: Int) = Http().bindAndHandle(routes, address, port)
}

object SlackServer {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("SlackServer")
    implicit val m = ActorMaterializer()
    implicit val executor = system.dispatcher
    implicit val ec = ExecutionContext

    val server = new SlackServer()
    server.startServer("localhost", 8080)
  }
}
