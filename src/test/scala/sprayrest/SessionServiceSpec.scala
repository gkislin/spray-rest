package sprayrest

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import domain._
import spray.http.{HttpEntity, HttpBody}
import spray.http.MediaTypes._


class SessionServiceSpec extends Specification with Specs2RouteTest with StorageMap with SessionService {
  def actorRefFactory = system

  import spray.json.DefaultJsonProtocol
  import spray.http._
  import MediaTypes._
  import JsonImplicits._
  import spray.httpx.SprayJsonSupport._
  import spray.httpx.unmarshalling._

  val jsonUser = """{
      "userId": 1    }"""
  val user_1 = User(1)

  "SessionService" should {

    "Unmarshall User" in {
      HttpEntity(`application/json`, jsonUser).as[User] === Right(user_1)
    }

    "return a SessionService for GET requests to the root path" in {
      Get() ~> route ~> check {
        entityAs[String] must contain("SessionService")
      }
    }

    "creates new session for user with given id" in {
      Post("/v1/sessions", HttpEntity(`application/json`, jsonUser)) ~> route ~> check {
        val session = entityAs[Session]
        session === getSessionDetail(session.sessionId).get.toSession
      }
    }

    "get session details" in {
      val session = controller.createSession(user_1)
      saveSessionDetail(session)
      Get("/v1/sessions/" + session.id) ~> route ~> check {
        entityAs[SessionDetail] === session
      }
    }

    "extends session to the Controller.SESSION_INTERVAL" in {
      val session = controller.createSession(user_1)
      saveSessionDetail(session)
      Put("/v1/sessions/" + session.id) ~> route ~> check {
        handled must beTrue
        getSessionDetail(session.id).get.expiredAt === controller.extendSession(session).expiredAt
      }
    }

    "terminates session with given id" in {
      val session = controller.createSession(user_1)
      saveSessionDetail(session)
      Delete("/v1/sessions/" + session.id) ~> route ~> check {
        handled must beTrue
        getSessionDetail(session.id) === None
      }
    }

    "get all sessions" in {
      val session = controller.createSession(user_1)
      saveSessionDetail(session)
      Get("/v1/sessions") ~> route ~> check {
        entityAs[List[SessionDetail]].find(_.id == session.id).get === session
      }
    }
  }
}