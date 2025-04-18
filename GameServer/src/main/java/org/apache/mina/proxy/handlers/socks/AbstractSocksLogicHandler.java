package org.apache.mina.proxy.handlers.socks;

import org.apache.mina.proxy.AbstractProxyLogicHandler;
import org.apache.mina.proxy.session.ProxyIoSession;

public abstract class AbstractSocksLogicHandler
extends AbstractProxyLogicHandler
{
protected final SocksProxyRequest request;

public AbstractSocksLogicHandler(ProxyIoSession proxyIoSession) {
super(proxyIoSession);
this.request = (SocksProxyRequest)proxyIoSession.getRequest();
}
}

