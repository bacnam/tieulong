package org.apache.http.impl.nio.bootstrap;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.nio.DefaultNHttpServerConnection;
import org.apache.http.impl.nio.DefaultNHttpServerConnectionFactory;
import org.apache.http.impl.nio.SSLNHttpServerConnectionFactory;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.NHttpConnectionFactory;
import org.apache.http.nio.protocol.HttpAsyncExpectationVerifier;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.nio.protocol.HttpAsyncRequestHandlerMapper;
import org.apache.http.nio.protocol.HttpAsyncService;
import org.apache.http.nio.protocol.UriHttpAsyncRequestHandlerMapper;
import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

public class ServerBootstrap
{
private int listenerPort;
private InetAddress localAddress;
private IOReactorConfig ioReactorConfig;
private ConnectionConfig connectionConfig;
private LinkedList<HttpRequestInterceptor> requestFirst;
private LinkedList<HttpRequestInterceptor> requestLast;
private LinkedList<HttpResponseInterceptor> responseFirst;
private LinkedList<HttpResponseInterceptor> responseLast;
private String serverInfo;
private HttpProcessor httpProcessor;
private ConnectionReuseStrategy connStrategy;
private HttpResponseFactory responseFactory;
private HttpAsyncRequestHandlerMapper handlerMapper;
private Map<String, HttpAsyncRequestHandler<?>> handlerMap;
private HttpAsyncExpectationVerifier expectationVerifier;
private SSLContext sslContext;
private SSLSetupHandler sslSetupHandler;
private NHttpConnectionFactory<? extends DefaultNHttpServerConnection> connectionFactory;
private ExceptionLogger exceptionLogger;

public static ServerBootstrap bootstrap() {
return new ServerBootstrap();
}

public final ServerBootstrap setListenerPort(int listenerPort) {
this.listenerPort = listenerPort;
return this;
}

public final ServerBootstrap setLocalAddress(InetAddress localAddress) {
this.localAddress = localAddress;
return this;
}

public final ServerBootstrap setIOReactorConfig(IOReactorConfig ioReactorConfig) {
this.ioReactorConfig = ioReactorConfig;
return this;
}

public final ServerBootstrap setConnectionConfig(ConnectionConfig connectionConfig) {
this.connectionConfig = connectionConfig;
return this;
}

public final ServerBootstrap setHttpProcessor(HttpProcessor httpProcessor) {
this.httpProcessor = httpProcessor;
return this;
}

public final ServerBootstrap addInterceptorFirst(HttpResponseInterceptor itcp) {
if (itcp == null) {
return this;
}
if (this.responseFirst == null) {
this.responseFirst = new LinkedList<HttpResponseInterceptor>();
}
this.responseFirst.addFirst(itcp);
return this;
}

public final ServerBootstrap addInterceptorLast(HttpResponseInterceptor itcp) {
if (itcp == null) {
return this;
}
if (this.responseLast == null) {
this.responseLast = new LinkedList<HttpResponseInterceptor>();
}
this.responseLast.addLast(itcp);
return this;
}

public final ServerBootstrap addInterceptorFirst(HttpRequestInterceptor itcp) {
if (itcp == null) {
return this;
}
if (this.requestFirst == null) {
this.requestFirst = new LinkedList<HttpRequestInterceptor>();
}
this.requestFirst.addFirst(itcp);
return this;
}

public final ServerBootstrap addInterceptorLast(HttpRequestInterceptor itcp) {
if (itcp == null) {
return this;
}
if (this.requestLast == null) {
this.requestLast = new LinkedList<HttpRequestInterceptor>();
}
this.requestLast.addLast(itcp);
return this;
}

public final ServerBootstrap setServerInfo(String serverInfo) {
this.serverInfo = serverInfo;
return this;
}

public final ServerBootstrap setConnectionReuseStrategy(ConnectionReuseStrategy connStrategy) {
this.connStrategy = connStrategy;
return this;
}

public final ServerBootstrap setResponseFactory(HttpResponseFactory responseFactory) {
this.responseFactory = responseFactory;
return this;
}

public final ServerBootstrap setHandlerMapper(HttpAsyncRequestHandlerMapper handlerMapper) {
this.handlerMapper = handlerMapper;
return this;
}

public final ServerBootstrap registerHandler(String pattern, HttpAsyncRequestHandler<?> handler) {
if (pattern == null || handler == null) {
return this;
}
if (this.handlerMap == null) {
this.handlerMap = new HashMap<String, HttpAsyncRequestHandler<?>>();
}
this.handlerMap.put(pattern, handler);
return this;
}

public final ServerBootstrap setExpectationVerifier(HttpAsyncExpectationVerifier expectationVerifier) {
this.expectationVerifier = expectationVerifier;
return this;
}

public final ServerBootstrap setConnectionFactory(NHttpConnectionFactory<? extends DefaultNHttpServerConnection> connectionFactory) {
this.connectionFactory = connectionFactory;
return this;
}

public final ServerBootstrap setSslContext(SSLContext sslContext) {
this.sslContext = sslContext;
return this;
}

public ServerBootstrap setSslSetupHandler(SSLSetupHandler sslSetupHandler) {
this.sslSetupHandler = sslSetupHandler;
return this;
}

public final ServerBootstrap setExceptionLogger(ExceptionLogger exceptionLogger) {
this.exceptionLogger = exceptionLogger;
return this; } public HttpServer create() {
UriHttpAsyncRequestHandlerMapper uriHttpAsyncRequestHandlerMapper;
DefaultConnectionReuseStrategy defaultConnectionReuseStrategy;
DefaultHttpResponseFactory defaultHttpResponseFactory;
DefaultNHttpServerConnectionFactory defaultNHttpServerConnectionFactory;
HttpProcessor httpProcessorCopy = this.httpProcessor;
if (httpProcessorCopy == null) {

HttpProcessorBuilder b = HttpProcessorBuilder.create();
if (this.requestFirst != null) {
for (HttpRequestInterceptor i : this.requestFirst) {
b.addFirst(i);
}
}
if (this.responseFirst != null) {
for (HttpResponseInterceptor i : this.responseFirst) {
b.addFirst(i);
}
}

String serverInfoCopy = this.serverInfo;
if (serverInfoCopy == null) {
serverInfoCopy = "Apache-HttpCore-NIO/1.1";
}

b.addAll(new HttpResponseInterceptor[] { (HttpResponseInterceptor)new ResponseDate(), (HttpResponseInterceptor)new ResponseServer(serverInfoCopy), (HttpResponseInterceptor)new ResponseContent(), (HttpResponseInterceptor)new ResponseConnControl() });

if (this.requestLast != null) {
for (HttpRequestInterceptor i : this.requestLast) {
b.addLast(i);
}
}
if (this.responseLast != null) {
for (HttpResponseInterceptor i : this.responseLast) {
b.addLast(i);
}
}
httpProcessorCopy = b.build();
} 

HttpAsyncRequestHandlerMapper handlerMapperCopy = this.handlerMapper;
if (handlerMapperCopy == null) {
UriHttpAsyncRequestHandlerMapper reqistry = new UriHttpAsyncRequestHandlerMapper();
if (this.handlerMap != null) {
for (Map.Entry<String, HttpAsyncRequestHandler<?>> entry : this.handlerMap.entrySet()) {
reqistry.register(entry.getKey(), entry.getValue());
}
}
uriHttpAsyncRequestHandlerMapper = reqistry;
} 

ConnectionReuseStrategy connStrategyCopy = this.connStrategy;
if (connStrategyCopy == null) {
defaultConnectionReuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
}

HttpResponseFactory responseFactoryCopy = this.responseFactory;
if (responseFactoryCopy == null) {
defaultHttpResponseFactory = DefaultHttpResponseFactory.INSTANCE;
}

NHttpConnectionFactory<? extends DefaultNHttpServerConnection> connectionFactoryCopy = this.connectionFactory;
if (connectionFactoryCopy == null) {
if (this.sslContext != null) {
SSLNHttpServerConnectionFactory sSLNHttpServerConnectionFactory = new SSLNHttpServerConnectionFactory(this.sslContext, this.sslSetupHandler, this.connectionConfig);
} else {

defaultNHttpServerConnectionFactory = new DefaultNHttpServerConnectionFactory(this.connectionConfig);
} 
}

ExceptionLogger exceptionLoggerCopy = this.exceptionLogger;
if (exceptionLoggerCopy == null) {
exceptionLoggerCopy = ExceptionLogger.NO_OP;
}

HttpAsyncService httpService = new HttpAsyncService(httpProcessorCopy, (ConnectionReuseStrategy)defaultConnectionReuseStrategy, (HttpResponseFactory)defaultHttpResponseFactory, (HttpAsyncRequestHandlerMapper)uriHttpAsyncRequestHandlerMapper, this.expectationVerifier, exceptionLoggerCopy);

return new HttpServer(this.listenerPort, this.localAddress, this.ioReactorConfig, httpService, (NHttpConnectionFactory<? extends DefaultNHttpServerConnection>)defaultNHttpServerConnectionFactory, exceptionLoggerCopy);
}
}

