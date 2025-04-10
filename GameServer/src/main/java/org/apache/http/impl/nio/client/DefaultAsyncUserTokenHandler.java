package org.apache.http.impl.nio.client;

import java.security.Principal;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpConnection;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.protocol.HttpContext;

@Immutable
public class DefaultAsyncUserTokenHandler
implements UserTokenHandler
{
public static final DefaultAsyncUserTokenHandler INSTANCE = new DefaultAsyncUserTokenHandler();

public Object getUserToken(HttpContext context) {
HttpClientContext clientContext = HttpClientContext.adapt(context);

Principal userPrincipal = null;

AuthState targetAuthState = clientContext.getTargetAuthState();
if (targetAuthState != null) {
userPrincipal = getAuthPrincipal(targetAuthState);
if (userPrincipal == null) {
AuthState proxyAuthState = clientContext.getProxyAuthState();
userPrincipal = getAuthPrincipal(proxyAuthState);
} 
} 

if (userPrincipal == null) {
HttpConnection conn = clientContext.getConnection();
if (conn.isOpen() && conn instanceof ManagedNHttpClientConnection) {
SSLSession sslsession = ((ManagedNHttpClientConnection)conn).getSSLSession();
if (sslsession != null) {
userPrincipal = sslsession.getLocalPrincipal();
}
} 
} 

return userPrincipal;
}

private static Principal getAuthPrincipal(AuthState authState) {
AuthScheme scheme = authState.getAuthScheme();
if (scheme != null && scheme.isComplete() && scheme.isConnectionBased()) {
Credentials creds = authState.getCredentials();
if (creds != null) {
return creds.getUserPrincipal();
}
} 
return null;
}
}

