package inbound

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import outbound.slack.responses.AuthTestResponse
import outbound.slack.SlackConsumer

import scala.concurrent.ExecutionContext

class SlackService(implicit s: ActorSystem, m: ActorMaterializer, ec: ExecutionContext) {

  import AuthTestResponse._

  val ws = new SlackConsumer

  val routes =
    pathPrefix("test") {
      path("auth") {
        get {
          parameters('token) { token =>
            complete {
              ws.authTest(token).map[ToResponseMarshallable] {
                case Right(authTestResponse) => authTestResponse
                case Left(errorMessage) => {
                  s.log.error(errorMessage)
                  BadRequest -> errorMessage
                }
              }
            }
          }
        }
      } ~
      path("api") {
        get {
          parameters('error.?, 'foo.?) { (error, foo) =>
            complete {
              ws.apiTest(error, foo).map[ToResponseMarshallable] {
                case Right(apiTestResponse) => apiTestResponse
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

}