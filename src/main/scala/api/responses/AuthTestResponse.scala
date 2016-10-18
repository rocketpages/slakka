package api.responses

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class AuthTestResponse(ok: Boolean, url: String, team: String, user: String, team_id: String, user_id: String)

object AuthTestResponse extends DefaultJsonProtocol {
  implicit val authTestJson: RootJsonFormat[AuthTestResponse] = jsonFormat6(AuthTestResponse.apply)
}



