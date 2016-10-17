package auth

import auth.Auth.{Token, ClientSecret, ClientId}

class Auth
class AuthorizedApp(clientId: ClientId, clientSecret: ClientSecret) extends Auth
class AuthorizedUser(token: Token) extends Auth

object Auth {
  type ClientId = String
  type ClientSecret = String
  type Token = String
}

object AuthorizedApp {
  def apply(clientId: ClientId, clientSecret: ClientSecret) = new AuthorizedApp(clientId, clientSecret)
}

object AuthorizedUser {
  def apply(token: Token) = new AuthorizedUser(token)
}
