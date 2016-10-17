package api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source, Flow}

import scala.concurrent.Future

class SlackOutgoing(implicit mat: ActorMaterializer, s: ActorSystem) {
  lazy val slackApiConnectionFlow: Flow[HttpRequest, HttpResponse, Any] = Http().outgoingConnectionHttps("slack.com")
  def slackApiRequest(request: HttpRequest): Future[HttpResponse] = Source.single(request).via(slackApiConnectionFlow).runWith(Sink.head)
}