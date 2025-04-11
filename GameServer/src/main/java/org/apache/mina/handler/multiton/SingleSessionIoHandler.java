package org.apache.mina.handler.multiton;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

@Deprecated
public interface SingleSessionIoHandler {
    void sessionCreated() throws Exception;

    void sessionOpened() throws Exception;

    void sessionClosed() throws Exception;

    void sessionIdle(IdleStatus paramIdleStatus) throws Exception;

    void exceptionCaught(Throwable paramThrowable) throws Exception;

    void inputClosed(IoSession paramIoSession);

    void messageReceived(Object paramObject) throws Exception;

    void messageSent(Object paramObject) throws Exception;
}

