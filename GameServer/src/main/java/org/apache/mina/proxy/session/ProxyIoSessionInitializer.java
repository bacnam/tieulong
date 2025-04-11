package org.apache.mina.proxy.session;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionInitializer;

public class ProxyIoSessionInitializer<T extends ConnectFuture>
        implements IoSessionInitializer<T> {
    private final IoSessionInitializer<T> wrappedSessionInitializer;
    private final ProxyIoSession proxyIoSession;

    public ProxyIoSessionInitializer(IoSessionInitializer<T> wrappedSessionInitializer, ProxyIoSession proxyIoSession) {
        this.wrappedSessionInitializer = wrappedSessionInitializer;
        this.proxyIoSession = proxyIoSession;
    }

    public ProxyIoSession getProxySession() {
        return this.proxyIoSession;
    }

    public void initializeSession(IoSession session, T future) {
        if (this.wrappedSessionInitializer != null) {
            this.wrappedSessionInitializer.initializeSession(session, (IoFuture) future);
        }

        if (this.proxyIoSession != null) {
            this.proxyIoSession.setSession(session);
            session.setAttribute(ProxyIoSession.PROXY_SESSION, this.proxyIoSession);
        }
    }
}

