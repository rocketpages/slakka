package api

import java.io.IOException
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import api.AuthTest.AuthTestResponse
import auth.Auth.Token
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.{ExecutionContext, Future}

class AuthTest(implicit ec: ExecutionContext, mat: ActorMaterializer, s: ActorSystem) extends SlackOutgoing {

  import api.Protocols._

  def authTest(token: Token): Future[Either[String, AuthTestResponse]] = {
    val req = HttpRequest(uri = s"/api/auth.test?token=${token}")
    s.log.debug(req.getUri().toString)
    slackApiRequest(req).flatMap { response =>
      response.status match {
        case OK => Unmarshal(response.entity).to[AuthTestResponse].map(Right(_))
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
}


object AuthTest {
  case class AuthTestResponse(ok: Boolean, url: String, team: String, user: String, team_id: String, user_id: String)
}