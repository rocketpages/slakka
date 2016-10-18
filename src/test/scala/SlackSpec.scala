import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import api.responses.AuthTestResponse
import org.scalatest._

class SlackSpec extends WordSpec with Matchers with ScalatestRouteTest {

  import AuthTestResponse._

  val service = new SlackService
  val token = sys.env("SLACK_TOKEN")

  "Auth API" should {
    "get user info for a test token" in {
      Get(s"/auth?token=${token}") ~> service.routes ~> check {
        status shouldBe OK
        contentType shouldBe `application/json`
        responseAs[AuthTestResponse] match {
          case AuthTestResponse(true, _, _, _, _, _) => assert(true)
          case _ => assert(false)
        }
      }
    }
  }
}
