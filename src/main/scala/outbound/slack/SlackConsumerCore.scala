package outbound.slack

import java.io.IOException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{Uri, HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import spray.json.RootJsonFormat

import scala.concurrent.{ExecutionContext, Future}

class SlackConsumerCore(implicit mat: ActorMaterializer, s: ActorSystem, ec: ExecutionContext) {
  private lazy val slackApiConnectionFlow: Flow[HttpRequest, HttpResponse, Any] = {
    Http().outgoingConnectionHttps("slack.com").async
  }

  protected def slackRequest[T: RootJsonFormat](uri: Uri, params: Option[Uri.Query] = None): Future[Either[String, T]] = {
    val req = params match {
      case Some(p) => HttpRequest().withUri(uri.withQuery(p))
      case _ => HttpRequest().withUri(uri)
    }
    slackApiRequest(req).flatMap(futureResponse[T])
  }

  private def futureResponse[T: RootJsonFormat]: (HttpResponse) => Future[Either[String, T]] = { response =>
    response.status match {
      case OK => Unmarshal(response.entity).to[T].map(Right(_))
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

  private def slackApiRequest(request: HttpRequest): Future[HttpResponse] = {
    Source.single(request).via(slackApiConnectionFlow).runWith(Sink.head)
  }
}