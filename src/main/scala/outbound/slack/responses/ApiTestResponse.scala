package outbound.slack.responses

import spray.json.{JsObject, JsArray, DefaultJsonProtocol, RootJsonFormat}

case class ApiTestResponse(ok: Boolean, error: Option[String], args: Option[JsObject])

object ApiTestResponse extends DefaultJsonProtocol {
  implicit val apiTestResponse: RootJsonFormat[ApiTestResponse] = jsonFormat3(ApiTestResponse.apply)
}
