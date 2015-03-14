package domain

import java.util.{UUID, Date}

/**
 * User: gkislin
 * Date: 03.10.13
 */
case class User(userId: Int)

case class Session(sessionId: String)

case class SessionMeta(createdAt: String, modifiedAt: String)

case class SessionDetail(id: String, userId: Int, expiredAt: String, meta: SessionMeta) {
  def toSession: Session = {
    new Session(id)
  }
}




