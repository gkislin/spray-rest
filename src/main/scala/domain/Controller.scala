package domain

import java.util.UUID
import TimeFormatter._

/**
 * User: gkislin
 * Date: 03.10.13
 */

class Controller(storage: Storage) {

  val SESSION_INTERVAL = 10000 // 10 sec

//  Test data (to check from browser)
//  createSession(User(5))
//  createSession(User(6))
//  createSession(User(7))

  def getSessions: List[SessionDetail] = storage.getSessions

  def getSession(sessionId: String): Option[SessionDetail] = storage.getSessionDetail(sessionId)

  def extendSession(session: SessionDetail): SessionDetail = {
    val newExpiredAt = toISO(toMs(session.expiredAt) + SESSION_INTERVAL)
    SessionDetail(session.id, session.userId, newExpiredAt, SessionMeta(session.meta.createdAt, getNow))
  }

  def updateSession(sessionId: String): Unit = {
    val session = storage.getSessionDetail(sessionId).get
    storage.updateSessionDetail(extendSession(session))
  }

  def terminateSession(sessionId: String): Unit = storage.deleteSessionDetail(sessionId)

  def deleteSession(sessionId: String): Option[SessionDetail] = storage.getSessionDetail(sessionId)

  def createSession(user: User): SessionDetail = {
    val now = System.currentTimeMillis()
    val isoDate = toISO(now)
    val s = new SessionDetail(UUID.randomUUID().toString, user.userId,
      toISO(now + SESSION_INTERVAL), SessionMeta(isoDate, isoDate))
    storage.saveSessionDetail(s)
    s
  }

}
