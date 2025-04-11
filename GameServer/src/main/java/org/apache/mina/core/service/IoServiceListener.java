package org.apache.mina.core.service;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.EventListener;

public interface IoServiceListener extends EventListener {
    void serviceActivated(IoService paramIoService) throws Exception;

    void serviceIdle(IoService paramIoService, IdleStatus paramIdleStatus) throws Exception;

    void serviceDeactivated(IoService paramIoService) throws Exception;

    void sessionCreated(IoSession paramIoSession) throws Exception;

    void sessionClosed(IoSession paramIoSession) throws Exception;

    void sessionDestroyed(IoSession paramIoSession) throws Exception;
}

