package org.apache.mina.core.service;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public interface IoHandler {
  void sessionCreated(IoSession paramIoSession) throws Exception;

  void sessionOpened(IoSession paramIoSession) throws Exception;

  void sessionClosed(IoSession paramIoSession) throws Exception;

  void sessionIdle(IoSession paramIoSession, IdleStatus paramIdleStatus) throws Exception;

  void exceptionCaught(IoSession paramIoSession, Throwable paramThrowable) throws Exception;

  void messageReceived(IoSession paramIoSession, Object paramObject) throws Exception;

  void messageSent(IoSession paramIoSession, Object paramObject) throws Exception;

  void inputClosed(IoSession paramIoSession) throws Exception;
}

