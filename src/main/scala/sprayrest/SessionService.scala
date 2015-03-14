package sprayrest

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.json.DefaultJsonProtocol
import domain._


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class SessionServiceActor extends Actor with StorageMap with SessionService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(route)
}

object JsonImplicits extends DefaultJsonProtocol {
  implicit val impSession = jsonFormat1(Session)
  implicit val impUser = jsonFormat1(User)
  implicit val impSessionMeta = jsonFormat2(SessionMeta)
  implicit val impSessionDetails = jsonFormat4(SessionDetail)
}

// this trait defines our service behavior independently from the service actor
trait SessionService extends HttpService {
  this: Storage =>

  val controller = new Controller(this)

  val route = {
    import JsonImplicits._
    import spray.httpx.SprayJsonSupport.{ sprayJsonMarshaller, sprayJsonUnmarshaller }

    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            <html>
              <body>
                <h1>The
                  <b>SessionService</b>
                  is running</h1>
              </body>
            </html>
          }
        }
      }
    } ~
    pathPrefix("v1" / "sessions"){
      path(".+".r) { sessionId =>
        get {
          complete {
            controller.getSession(sessionId)
          }
        } ~
        put {
          complete{
            controller.updateSession(sessionId)
            "{}"
          }
        } ~
        delete {
          complete{
            controller.terminateSession(sessionId)
            "{}"
          }
        }
      } ~
      path("") {
        post {
          entity(as[User]) {
            user =>
              complete{
                controller.createSession(user).toSession
              }
          }
        } ~
        path("") {
          get {
            complete {
              controller.getSessions
            }
          }
        }
      }
    }
  }
}