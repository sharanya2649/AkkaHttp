package com.techsophy.akka.oauth2

import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import org.keycloak.representations.AccessToken


class OAuth2Authorization(tokenVerifier: TokenVerifier) {

  def authorized: Directive1[OAuth2Token] = {
    bearerToken.flatMap {
      case Some(token) =>
        onComplete(tokenVerifier.verifyToken(token)).flatMap {
          _.map(token => provide(OAuth2Token(token)))
            .recover {
              case ex =>
                reject(AuthorizationFailedRejection).toDirective[Tuple1[OAuth2Token]]
            }
            .get
        }
      case None =>
        reject(AuthorizationFailedRejection)
    }
  }

  private def bearerToken: Directive1[Option[String]] =
    for {
      authBearerHeader <- optionalHeaderValueByType(classOf[Authorization]).map(extractBearerToken)
      xAuthCookie <- optionalCookie("X-Authorization-Token").map(_.map(_.value))
    } yield authBearerHeader.orElse(xAuthCookie)

  private def extractBearerToken(authHeader: Option[Authorization]): Option[String] =
    authHeader.collect {
      case Authorization(OAuth2BearerToken(token)) => token
    }

}

case class OAuth2Token(token: AccessToken)
