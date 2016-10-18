package api

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import api.responses.AuthTestResponse
import auth.Auth.Token
import akka.http.scaladsl.model.{Uri, HttpRequest}

import scala.concurrent.{ExecutionContext, Future}

class AuthTest(implicit ec: ExecutionContext, mat: ActorMaterializer, s: ActorSystem) extends SlackOutgoing {

  def authTest(token: Token): Future[Either[String, AuthTestResponse]] = {
    val req = HttpRequest().withUri(Uri("https://slack.com/api/auth.test").withQuery(Uri.Query(Map("token" -> token))))
    slackApiRequest(req).flatMap(futureResponse[AuthTestResponse])
  }


}

