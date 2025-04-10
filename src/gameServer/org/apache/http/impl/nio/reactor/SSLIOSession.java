package org.apache.http.impl.nio.reactor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.ssl.SSLIOSession;
import org.apache.http.nio.reactor.ssl.SSLMode;
import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
import org.apache.http.params.HttpParams;

@Deprecated
public class SSLIOSession
extends SSLIOSession
{
public SSLIOSession(IOSession session, SSLContext sslContext, SSLSetupHandler handler) {
super(session, SSLMode.CLIENT, sslContext, (handler != null) ? new SSLSetupHandlerAdaptor(handler) : null);
}

public SSLIOSession(IOSession session, SSLContext sslContext, SSLIOSessionHandler handler) {
super(session, SSLMode.CLIENT, sslContext, (handler != null) ? new SSLIOSessionHandlerAdaptor(handler) : null);
}

public synchronized void bind(SSLMode mode, HttpParams params) throws SSLException {
SSLSetupHandler handler = getSSLSetupHandler();
if (handler instanceof SSLIOSessionHandlerAdaptor) {
((SSLIOSessionHandlerAdaptor)handler).setParams(params);
} else if (handler instanceof SSLSetupHandlerAdaptor) {
((SSLSetupHandlerAdaptor)handler).setParams(params);
} 
initialize(convert(mode));
}

private SSLMode convert(SSLMode mode) {
switch (mode) {
case CLIENT:
return SSLMode.CLIENT;
case SERVER:
return SSLMode.SERVER;
} 
return null;
}
}

