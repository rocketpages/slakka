package api

import java.io.IOException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source, Flow}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.unmarshalling.Unmarshal
import spray.json.RootJsonFormat

import scala.concurrent.{ExecutionContext, Future}

class SlackOutgoing(implicit mat: ActorMaterializer, s: ActorSystem, ec: ExecutionContext) {
  lazy val slackApiConnectionFlow: Flow[HttpRequest, HttpResponse, Any] = Http().outgoingConnectionHttps("slack.com")
  def slackApiRequest(request: HttpRequest): Future[HttpResponse] = Source.single(request).via(slackApiConnectionFlow).runWith(Sink.head)

  def futureResponse[A: RootJsonFormat]: (HttpResponse) => Future[Either[String, A]] = { response =>
    response.status match {
      case OK => Unmarshal(response.entity).to[A].map(Right(_))
      case BadRequest => Unmarshal(response.entity).to[String].flatMap { entity =>
        val error = s"400 Bad Request with response: $entity"
        Future.successful(Left(error))
      }
      case _ => Unmarshal(response.entity).to[String].flatMap { entity =>
        val error = s"Slack auth.test request failed with status code ${response.status} and entity $entity"
        Future.failed(new IOException(error))
      }
    }
  }
}