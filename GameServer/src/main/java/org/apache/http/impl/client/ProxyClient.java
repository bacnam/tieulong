package org.apache.http.impl.client;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.params.HttpClientParamConfig;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.execchain.TunnelRefusedException;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

public class ProxyClient
{
private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
private final ConnectionConfig connectionConfig;
private final RequestConfig requestConfig;
private final HttpProcessor httpProcessor;
private final HttpRequestExecutor requestExec;
private final ProxyAuthenticationStrategy proxyAuthStrategy;
private final HttpAuthenticator authenticator;
private final AuthState proxyAuthState;
private final AuthSchemeRegistry authSchemeRegistry;
private final ConnectionReuseStrategy reuseStrategy;

public ProxyClient(HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, ConnectionConfig connectionConfig, RequestConfig requestConfig) {
this.connFactory = (connFactory != null) ? connFactory : (HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection>)ManagedHttpClientConnectionFactory.INSTANCE;
this.connectionConfig = (connectionConfig != null) ? connectionConfig : ConnectionConfig.DEFAULT;
this.requestConfig = (requestConfig != null) ? requestConfig : RequestConfig.DEFAULT;
this.httpProcessor = (HttpProcessor)new ImmutableHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent() });

this.requestExec = new HttpRequestExecutor();
this.proxyAuthStrategy = new ProxyAuthenticationStrategy();
this.authenticator = new HttpAuthenticator();
this.proxyAuthState = new AuthState();
this.authSchemeRegistry = new AuthSchemeRegistry();
this.authSchemeRegistry.register("Basic", (AuthSchemeFactory)new BasicSchemeFactory());
this.authSchemeRegistry.register("Digest", (AuthSchemeFactory)new DigestSchemeFactory());
this.authSchemeRegistry.register("NTLM", (AuthSchemeFactory)new NTLMSchemeFactory());
this.authSchemeRegistry.register("Negotiate", (AuthSchemeFactory)new SPNegoSchemeFactory());
this.authSchemeRegistry.register("Kerberos", (AuthSchemeFactory)new KerberosSchemeFactory());
this.reuseStrategy = (ConnectionReuseStrategy)new DefaultConnectionReuseStrategy();
}

@Deprecated
public ProxyClient(HttpParams params) {
this(null, HttpParamConfig.getConnectionConfig(params), HttpClientParamConfig.getRequestConfig(params));
}

public ProxyClient(RequestConfig requestConfig) {
this(null, null, requestConfig);
}

public ProxyClient() {
this(null, null, null);
}

@Deprecated
public HttpParams getParams() {
return (HttpParams)new BasicHttpParams();
}

@Deprecated
public AuthSchemeRegistry getAuthSchemeRegistry() {
return this.authSchemeRegistry;
}

public Socket tunnel(HttpHost proxy, HttpHost target, Credentials credentials) throws IOException, HttpException {
HttpResponse response;
Args.notNull(proxy, "Proxy host");
Args.notNull(target, "Target host");
Args.notNull(credentials, "Credentials");
HttpHost host = target;
if (host.getPort() <= 0) {
host = new HttpHost(host.getHostName(), 80, host.getSchemeName());
}
HttpRoute route = new HttpRoute(host, this.requestConfig.getLocalAddress(), proxy, false, RouteInfo.TunnelType.TUNNELLED, RouteInfo.LayerType.PLAIN);

ManagedHttpClientConnection conn = (ManagedHttpClientConnection)this.connFactory.create(route, this.connectionConfig);

BasicHttpContext basicHttpContext = new BasicHttpContext();

BasicHttpRequest basicHttpRequest = new BasicHttpRequest("CONNECT", host.toHostString(), (ProtocolVersion)HttpVersion.HTTP_1_1);

BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
credsProvider.setCredentials(new AuthScope(proxy), credentials);

basicHttpContext.setAttribute("http.target_host", target);
basicHttpContext.setAttribute("http.connection", conn);
basicHttpContext.setAttribute("http.request", basicHttpRequest);
basicHttpContext.setAttribute("http.route", route);
basicHttpContext.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
basicHttpContext.setAttribute("http.auth.credentials-provider", credsProvider);
basicHttpContext.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
basicHttpContext.setAttribute("http.request-config", this.requestConfig);

this.requestExec.preProcess((HttpRequest)basicHttpRequest, this.httpProcessor, (HttpContext)basicHttpContext);

while (true) {
if (!conn.isOpen()) {
Socket socket = new Socket(proxy.getHostName(), proxy.getPort());
conn.bind(socket);
} 

this.authenticator.generateAuthResponse((HttpRequest)basicHttpRequest, this.proxyAuthState, (HttpContext)basicHttpContext);

response = this.requestExec.execute((HttpRequest)basicHttpRequest, (HttpClientConnection)conn, (HttpContext)basicHttpContext);

int i = response.getStatusLine().getStatusCode();
if (i < 200) {
throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
}

if (this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, (HttpContext)basicHttpContext))
{
if (this.authenticator.handleAuthChallenge(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, (HttpContext)basicHttpContext)) {

if (this.reuseStrategy.keepAlive(response, (HttpContext)basicHttpContext)) {

HttpEntity entity = response.getEntity();
EntityUtils.consume(entity);
} else {
conn.close();
} 

basicHttpRequest.removeHeaders("Proxy-Authorization");

continue;
} 
}

break;
} 

int status = response.getStatusLine().getStatusCode();

if (status > 299) {

HttpEntity entity = response.getEntity();
if (entity != null) {
response.setEntity((HttpEntity)new BufferedHttpEntity(entity));
}

conn.close();
throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
} 

return conn.getSocket();
}
}

