package outbound.slack

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, Uri}
import akka.stream.ActorMaterializer
import auth.Auth.Token
import outbound.slack.responses.AuthTestResponse

import scala.concurrent.{ExecutionContext, Future}

class AuthTest(implicit ec: ExecutionContext, mat: ActorMaterializer, s: ActorSystem) extends SlackOutgoing {

  def authTest(token: Token): Future[Either[String, AuthTestResponse]] = {
    val req = HttpRequest().withUri(Uri("/api/auth.test").withQuery(Uri.Query(Map("token" -> token))))
    slackApiRequest(req).flatMap(futureResponse[AuthTestResponse])
  }

}

