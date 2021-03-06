package com.softwaremill.supler_example.auth

import org.scalatra.SweetCookies
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import com.softwaremill.supler_example.rest.UsersServlet
import com.softwaremill.supler_example.service.user.UserService
import com.softwaremill.supler_example.service.data.UserJson
import org.scalatra.test.scalatest.ScalatraFlatSpec
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.BDDMockito._

class RememberMeStrategySpec extends ScalatraFlatSpec with MockitoSugar {
  behavior of "RememberMe"

  implicit val httpResponse = mock[HttpServletResponse]
  implicit val httpRequest = mock[HttpServletRequest]
  val app = mock[UsersServlet]
  val userService = mock[UserService]
  val loggedUser: UserJson = UserJson("1" * 24, "admin", "admin@admin.net", "token")
  when(userService.authenticateWithToken(loggedUser.token)) thenReturn(Option(loggedUser))

  val rememberMe = true
  val strategy = new RememberMeStrategy(app, rememberMe, userService)

  it should "authenticate user base on cookie" in {
    // Given
    given(app.cookies) willReturn new SweetCookies(Map(("rememberMe", loggedUser.token)), httpResponse)

    // When
    val user: Option[UserJson] = strategy.authenticate()

    // Then
    user should not be (None)
    user.get.login should be ("admin")
  }

  it should "not authenticate user with invalid cookie" in {
    // Given
    given(app.cookies) willReturn new SweetCookies(Map(("rememberMe", loggedUser.token + "X")), httpResponse)

    // When
    val user: Option[UserJson] = strategy.authenticate()

    // Then
    user should be (null)
  }
}
