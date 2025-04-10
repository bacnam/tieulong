package org.apache.mina.proxy.handlers.http;

import org.apache.mina.proxy.ProxyAuthException;
import org.apache.mina.proxy.handlers.http.basic.HttpBasicAuthLogicHandler;
import org.apache.mina.proxy.handlers.http.basic.HttpNoAuthLogicHandler;
import org.apache.mina.proxy.handlers.http.digest.HttpDigestAuthLogicHandler;
import org.apache.mina.proxy.handlers.http.ntlm.HttpNTLMAuthLogicHandler;
import org.apache.mina.proxy.session.ProxyIoSession;

public enum HttpAuthenticationMethods
{
NO_AUTH(1), BASIC(2), NTLM(3), DIGEST(4);

private final int id;

HttpAuthenticationMethods(int id) {
this.id = id;
}

public int getId() {
return this.id;
}

public AbstractAuthLogicHandler getNewHandler(ProxyIoSession proxyIoSession) throws ProxyAuthException {
return getNewHandler(this.id, proxyIoSession);
}

public static AbstractAuthLogicHandler getNewHandler(int method, ProxyIoSession proxyIoSession) throws ProxyAuthException {
if (method == BASIC.id)
return (AbstractAuthLogicHandler)new HttpBasicAuthLogicHandler(proxyIoSession); 
if (method == DIGEST.id)
return (AbstractAuthLogicHandler)new HttpDigestAuthLogicHandler(proxyIoSession); 
if (method == NTLM.id)
return (AbstractAuthLogicHandler)new HttpNTLMAuthLogicHandler(proxyIoSession); 
if (method == NO_AUTH.id) {
return (AbstractAuthLogicHandler)new HttpNoAuthLogicHandler(proxyIoSession);
}
return null;
}
}

