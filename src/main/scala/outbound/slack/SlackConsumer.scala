package outbound.slack

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, Uri}
import akka.stream.ActorMaterializer
import auth.Auth.Token
import outbound.slack.responses.{ApiTestResponse, AuthTestResponse}

import scala.concurrent.{ExecutionContext, Future}

class SlackConsumer(implicit ec: ExecutionContext, mat: ActorMaterializer, s: ActorSystem) extends SlackConsumerCore {

  def authTest(token: Token): Future[Either[String, AuthTestResponse]] = {
    slackRequest[AuthTestResponse](Uri("/api/auth.test"), Some(Uri.Query(Map("token" -> token))))
  }

  def apiTest: Future[Either[String, ApiTestResponse]] = {
    slackRequest[ApiTestResponse](Uri("/api/api.test"))
  }

}

