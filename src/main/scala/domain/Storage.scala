package domain

import scala.collection.concurrent.TrieMap

/**
 * User: gkislin
 * Date: 02.10.13
 */

trait Storage {
  def saveSessionDetail(session: SessionDetail): Unit

  def getSessionDetail(sessionId: String): Option[SessionDetail]

  def deleteSessionDetail(sessionId: String): Unit

  def updateSessionDetail(session: SessionDetail): Unit

  def getSessions: List[SessionDetail]
}

trait StorageMap extends Storage {
  private val USER_SESSION_MAP = TrieMap[String, SessionDetail]() // kepp data in concurrent map

  override def saveSessionDetail(session: SessionDetail) = USER_SESSION_MAP.put(session.id, session)

  override def getSessionDetail(sessionId: String): Option[SessionDetail] = USER_SESSION_MAP.get(sessionId)

  override def deleteSessionDetail(sessionId: String): Unit = USER_SESSION_MAP.remove(sessionId)

  override def updateSessionDetail(session: SessionDetail): Unit = USER_SESSION_MAP.put(session.id, session)

  override def getSessions: List[SessionDetail] = USER_SESSION_MAP.values.toList
}


