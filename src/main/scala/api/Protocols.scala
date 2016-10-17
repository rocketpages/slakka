package api

import api.AuthTest.AuthTestResponse
import spray.json.DefaultJsonProtocol

object Protocols extends DefaultJsonProtocol {
  implicit val authTestFormat = jsonFormat6(AuthTestResponse.apply)
}
