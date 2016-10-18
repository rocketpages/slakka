package inbound

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import outbound.slack.responses.AuthTestResponse
import outbound.slack.AuthTest

import scala.concurrent.ExecutionContext

class SlackService(implicit s: ActorSystem, m: ActorMaterializer, ec: ExecutionContext) {

  import AuthTestResponse._

  val authApi = new AuthTest

  val routes =
    path("auth") {
      get {
        parameters('token) { token =>
          complete {
            authApi.authTest(token).map[ToResponseMarshallable] {
              case Right(authTestResponse) => authTestResponse
              case Left(errorMessage) => {
                s.log.error(errorMessage)
                BadRequest -> errorMessage
              }
            }
          }
        }
      }
    }

}