import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import outbound.slack.responses.{ApiTestResponse, AuthTestResponse}
import inbound.SlackService
import org.scalatest._

class SlackSpec extends WordSpec with Matchers with ScalatestRouteTest {

  import AuthTestResponse._

  val service = new SlackService
  val token = sys.env("SLACK_TOKEN")

  "Test API" should {
    "successfully test slack auth.test endpoint" in {
      Get(s"/test/auth?token=${token}") ~> service.routes ~> check {
        status shouldBe OK
        contentType shouldBe `application/json`
        responseAs[AuthTestResponse] match {
          case AuthTestResponse(true, _, _, _, _, _) => assert(true)
          case _ => assert(false)
        }
      }
    }
    "successfully test slack api.test endpoint with no args" in {
      Get(s"/test/api") ~> service.routes ~> check {
        status shouldBe OK
        contentType shouldBe `application/json`
        responseAs[ApiTestResponse] match {
          case ApiTestResponse(true, _, _) => assert(true)
          case _ => assert(false)
        }
      }
    }
    "successfully test slack api.test endpoint with error" in {
      Get(s"/test/api?error=error&foo=bar") ~> service.routes ~> check {
        status shouldBe OK
        contentType shouldBe `application/json`
        responseAs[ApiTestResponse] match {
          case ApiTestResponse(false, Some("error"), _) => assert(true)
          case _ => assert(false)
        }
      }
    }
    "successfully test slack api.test endpoint with args" in {
      Get(s"/test/api?foo=bar") ~> service.routes ~> check {
        status shouldBe OK
        contentType shouldBe `application/json`
        responseAs[ApiTestResponse] match {
          case ApiTestResponse(true, _, _) => assert(true)
          case _ => assert(false)
        }
      }
    }
  }
}
