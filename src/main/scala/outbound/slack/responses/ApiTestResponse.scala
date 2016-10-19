package outbound.slack.responses

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class ApiTestResponse(ok: Boolean, args: Option[List[String]], error: Option[String])

object ApiTestResponse extends DefaultJsonProtocol {
  implicit val apiTestJson: RootJsonFormat[ApiTestResponse] = jsonFormat3(ApiTestResponse.apply)
}
