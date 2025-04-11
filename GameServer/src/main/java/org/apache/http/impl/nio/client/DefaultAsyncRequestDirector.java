package org.apache.http.impl.nio.client;

import org.apache.commons.logging.Log;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.conn.routing.*;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.conn.ClientAsyncConnectionManager;
import org.apache.http.nio.conn.ManagedClientAsyncConnection;
import org.apache.http.nio.conn.scheme.AsyncScheme;
import org.apache.http.nio.conn.scheme.AsyncSchemeRegistry;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutionHandler;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Deprecated
class DefaultAsyncRequestDirector<T>
        implements HttpAsyncRequestExecutionHandler<T> {
    private static final AtomicLong COUNTER = new AtomicLong(1L);

    private final Log log;

    private final HttpAsyncRequestProducer requestProducer;

    private final HttpAsyncResponseConsumer<T> responseConsumer;

    private final HttpContext localContext;

    private final ResultCallback<T> resultCallback;

    private final ClientAsyncConnectionManager connmgr;

    private final HttpProcessor httppocessor;

    private final HttpRoutePlanner routePlanner;

    private final HttpRouteDirector routeDirector;

    private final ConnectionReuseStrategy reuseStrategy;

    private final ConnectionKeepAliveStrategy keepaliveStrategy;

    private final RedirectStrategy redirectStrategy;

    private final AuthenticationStrategy targetAuthStrategy;

    private final AuthenticationStrategy proxyAuthStrategy;

    private final UserTokenHandler userTokenHandler;

    private final AuthState targetAuthState;

    private final AuthState proxyAuthState;

    private final HttpAuthenticator authenticator;

    private final HttpParams clientParams;

    private final long id;

    private volatile boolean closed;
    private volatile InternalFutureCallback connRequestCallback;
    private volatile ManagedClientAsyncConnection managedConn;
    private RoutedRequest mainRequest;
    private RoutedRequest followup;
    private HttpResponse finalResponse;
    private ClientParamsStack params;
    private RequestWrapper currentRequest;
    private HttpResponse currentResponse;
    private boolean routeEstablished;
    private int redirectCount;
    private ByteBuffer tmpbuf;
    private boolean requestContentProduced;
    private boolean requestSent;
    private int execCount;

    public DefaultAsyncRequestDirector(Log log, HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext localContext, ResultCallback<T> callback, ClientAsyncConnectionManager connmgr, HttpProcessor httppocessor, HttpRoutePlanner routePlanner, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepaliveStrategy, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams clientParams) {
        this.log = log;
        this.requestProducer = requestProducer;
        this.responseConsumer = responseConsumer;
        this.localContext = localContext;
        this.resultCallback = callback;
        this.connmgr = connmgr;
        this.httppocessor = httppocessor;
        this.routePlanner = routePlanner;
        this.reuseStrategy = reuseStrategy;
        this.keepaliveStrategy = keepaliveStrategy;
        this.redirectStrategy = redirectStrategy;
        this.routeDirector = (HttpRouteDirector) new BasicRouteDirector();
        this.targetAuthStrategy = targetAuthStrategy;
        this.proxyAuthStrategy = proxyAuthStrategy;
        this.userTokenHandler = userTokenHandler;
        this.targetAuthState = new AuthState();
        this.proxyAuthState = new AuthState();
        this.authenticator = new HttpAuthenticator(log);
        this.clientParams = clientParams;
        this.id = COUNTER.getAndIncrement();
    }

    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        ManagedClientAsyncConnection localConn = this.managedConn;
        if (localConn != null) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("[exchange: " + this.id + "] aborting connection " + localConn);
            }
            try {
                localConn.abortConnection();
            } catch (IOException ioex) {
                this.log.debug("I/O error releasing connection", ioex);
            }
        }
        try {
            this.requestProducer.close();
        } catch (IOException ex) {
            this.log.debug("I/O error closing request producer", ex);
        }
        try {
            this.responseConsumer.close();
        } catch (IOException ex) {
            this.log.debug("I/O error closing response consumer", ex);
        }
    }

    public synchronized void start() {
        try {
            if (this.log.isDebugEnabled()) {
                this.log.debug("[exchange: " + this.id + "] start execution");
            }
            this.localContext.setAttribute("http.auth.target-scope", this.targetAuthState);
            this.localContext.setAttribute("http.auth.proxy-scope", this.proxyAuthState);

            HttpHost target = this.requestProducer.getTarget();
            HttpRequest request = this.requestProducer.generateRequest();
            if (request instanceof AbortableHttpRequest) {
                ((AbortableHttpRequest) request).setReleaseTrigger(new ConnectionReleaseTrigger() {
                    public void releaseConnection() throws IOException {
                    }

                    public void abortConnection() throws IOException {
                        DefaultAsyncRequestDirector.this.cancel();
                    }
                });
            }

            this.params = new ClientParamsStack(null, this.clientParams, request.getParams(), null);
            RequestWrapper wrapper = wrapRequest(request);
            wrapper.setParams((HttpParams) this.params);
            HttpRoute route = determineRoute(target, (HttpRequest) wrapper, this.localContext);
            this.mainRequest = new RoutedRequest(wrapper, route);
            RequestConfig config = ParamConfig.getRequestConfig((HttpParams) this.params);
            this.localContext.setAttribute("http.request-config", config);
            this.requestContentProduced = false;
            requestConnection();
        } catch (Exception ex) {
            failed(ex);
        }
    }

    public HttpHost getTarget() {
        return this.requestProducer.getTarget();
    }

    public synchronized HttpRequest generateRequest() throws IOException, HttpException {
        HttpRoute route = this.mainRequest.getRoute();
        if (!this.routeEstablished) {
            int step;
            do {
                HttpRequest connect;
                HttpRoute fact = this.managedConn.getRoute();
                step = this.routeDirector.nextStep((RouteInfo) route, (RouteInfo) fact);
                switch (step) {
                    case 1:
                    case 2:
                        break;
                    case 3:
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("[exchange: " + this.id + "] Tunnel required");
                        }
                        connect = createConnectRequest(route);
                        this.currentRequest = wrapRequest(connect);
                        this.currentRequest.setParams((HttpParams) this.params);
                        break;
                    case 4:
                        throw new HttpException("Proxy chains are not supported");
                    case 5:
                        this.managedConn.layerProtocol(this.localContext, (HttpParams) this.params);
                        break;
                    case -1:
                        throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);

                    case 0:
                        this.routeEstablished = true;
                        break;
                    default:
                        throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
                }

            } while (step > 0 && this.currentRequest == null);
        }

        HttpHost target = (HttpHost) this.params.getParameter("http.virtual-host");
        if (target == null) {
            target = route.getTargetHost();
        }
        HttpHost proxy = route.getProxyHost();
        this.localContext.setAttribute("http.target_host", target);
        this.localContext.setAttribute("http.proxy_host", proxy);
        this.localContext.setAttribute("http.connection", this.managedConn);
        this.localContext.setAttribute("http.route", route);

        if (this.currentRequest == null) {
            this.currentRequest = this.mainRequest.getRequest();

            String userinfo = this.currentRequest.getURI().getUserInfo();
            if (userinfo != null) {
                this.targetAuthState.update((AuthScheme) new BasicScheme(), (Credentials) new UsernamePasswordCredentials(userinfo));
            }

            rewriteRequestURI(this.currentRequest, route);
        }

        this.currentRequest.resetHeaders();

        this.currentRequest.incrementExecCount();
        if (this.currentRequest.getExecCount() > 1 && !this.requestProducer.isRepeatable() && this.requestContentProduced) {

            throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
        }

        this.execCount++;
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] Attempt " + this.execCount + " to execute request");
        }
        return (HttpRequest) this.currentRequest;
    }

    public synchronized void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] produce content");
        }
        this.requestContentProduced = true;
        this.requestProducer.produceContent(encoder, ioctrl);
        if (encoder.isCompleted()) {
            this.requestProducer.resetRequest();
        }
    }

    public void requestCompleted(HttpContext context) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] Request completed");
        }
        this.requestSent = true;
        this.requestProducer.requestCompleted(context);
    }

    public boolean isRepeatable() {
        return this.requestProducer.isRepeatable();
    }

    public void resetRequest() throws IOException {
        this.requestSent = false;
        this.requestProducer.resetRequest();
    }

    public synchronized void responseReceived(HttpResponse response) throws IOException, HttpException {
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] Response received " + response.getStatusLine());
        }
        this.currentResponse = response;
        this.currentResponse.setParams((HttpParams) this.params);

        int status = this.currentResponse.getStatusLine().getStatusCode();

        if (!this.routeEstablished) {
            String method = this.currentRequest.getMethod();
            if (method.equalsIgnoreCase("CONNECT") && status == 200) {
                this.managedConn.tunnelTarget((HttpParams) this.params);
            } else {
                this.followup = handleConnectResponse();
                if (this.followup == null) {
                    this.finalResponse = response;
                }
            }
        } else {
            this.followup = handleResponse();
            if (this.followup == null) {
                this.finalResponse = response;
            }

            Object userToken = this.localContext.getAttribute("http.user-token");
            if (this.managedConn != null) {
                if (userToken == null) {
                    userToken = this.userTokenHandler.getUserToken(this.localContext);
                    this.localContext.setAttribute("http.user-token", userToken);
                }
                if (userToken != null) {
                    this.managedConn.setState(userToken);
                }
            }
        }
        if (this.finalResponse != null) {
            this.responseConsumer.responseReceived(response);
        }
    }

    public synchronized void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] Consume content");
        }
        if (this.finalResponse != null) {
            this.responseConsumer.consumeContent(decoder, ioctrl);
        } else {
            if (this.tmpbuf == null) {
                this.tmpbuf = ByteBuffer.allocate(2048);
            }
            this.tmpbuf.clear();
            decoder.read(this.tmpbuf);
        }
    }

    private void releaseConnection() {
        if (this.managedConn != null) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("[exchange: " + this.id + "] releasing connection " + this.managedConn);
            }
            try {
                this.managedConn.getContext().removeAttribute("http.nio.exchange-handler");
                this.managedConn.releaseConnection();
            } catch (IOException ioex) {
                this.log.debug("I/O error releasing connection", ioex);
            }
            this.managedConn = null;
        }
    }

    public synchronized void failed(Exception ex) {
        try {
            if (!this.requestSent) {
                this.requestProducer.failed(ex);
            }
            this.responseConsumer.failed(ex);
        } finally {
            try {
                this.resultCallback.failed(ex, this);
            } finally {
                close();
            }
        }
    }

    public synchronized void responseCompleted(HttpContext context) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] Response fully read");
        }
        try {
            if (this.resultCallback.isDone()) {
                return;
            }
            if (this.managedConn.isOpen()) {
                long duration = this.keepaliveStrategy.getKeepAliveDuration(this.currentResponse, this.localContext);

                if (this.log.isDebugEnabled()) {
                    String s;
                    if (duration > 0L) {
                        s = "for " + duration + " " + TimeUnit.MILLISECONDS;
                    } else {
                        s = "indefinitely";
                    }
                    this.log.debug("[exchange: " + this.id + "] Connection can be kept alive " + s);
                }
                this.managedConn.setIdleDuration(duration, TimeUnit.MILLISECONDS);
            } else {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("[exchange: " + this.id + "] Connection cannot be kept alive");
                }
                this.managedConn.unmarkReusable();
                if (this.proxyAuthState.getState() == AuthProtocolState.SUCCESS && this.proxyAuthState.getAuthScheme() != null && this.proxyAuthState.getAuthScheme().isConnectionBased()) {

                    if (this.log.isDebugEnabled()) {
                        this.log.debug("[exchange: " + this.id + "] Resetting proxy auth state");
                    }
                    this.proxyAuthState.reset();
                }
                if (this.targetAuthState.getState() == AuthProtocolState.SUCCESS && this.targetAuthState.getAuthScheme() != null && this.targetAuthState.getAuthScheme().isConnectionBased()) {

                    if (this.log.isDebugEnabled()) {
                        this.log.debug("[exchange: " + this.id + "] Resetting target auth state");
                    }
                    this.targetAuthState.reset();
                }
            }

            if (this.finalResponse != null) {
                this.responseConsumer.responseCompleted(this.localContext);
                if (this.log.isDebugEnabled()) {
                    this.log.debug("[exchange: " + this.id + "] Response processed");
                }
                releaseConnection();
                T result = (T) this.responseConsumer.getResult();
                Exception ex = this.responseConsumer.getException();
                if (ex == null) {
                    this.resultCallback.completed(result, this);
                } else {
                    this.resultCallback.failed(ex, this);
                }
            } else {
                if (this.followup != null) {
                    HttpRoute actualRoute = this.mainRequest.getRoute();
                    HttpRoute newRoute = this.followup.getRoute();
                    if (!actualRoute.equals(newRoute)) {
                        releaseConnection();
                    }
                    this.mainRequest = this.followup;
                }
                if (this.managedConn != null && !this.managedConn.isOpen()) {
                    releaseConnection();
                }
                if (this.managedConn != null) {
                    this.managedConn.requestOutput();
                } else {
                    requestConnection();
                }
            }
            this.followup = null;
            this.currentRequest = null;
            this.currentResponse = null;
        } catch (RuntimeException runex) {
            failed(runex);
            throw runex;
        }
    }

    public synchronized boolean cancel() {
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] Cancelled");
        }
        try {
            boolean cancelled = this.responseConsumer.cancel();

            T result = (T) this.responseConsumer.getResult();
            Exception ex = this.responseConsumer.getException();
            if (ex != null) {
                this.resultCallback.failed(ex, this);
            } else if (result != null) {
                this.resultCallback.completed(result, this);
            } else {
                this.resultCallback.cancelled(this);
            }
            return cancelled;
        } catch (RuntimeException runex) {
            this.resultCallback.failed(runex, this);
            throw runex;
        } finally {
            close();
        }
    }

    public boolean isDone() {
        return this.resultCallback.isDone();
    }

    public T getResult() {
        return (T) this.responseConsumer.getResult();
    }

    public Exception getException() {
        return this.responseConsumer.getException();
    }

    private synchronized void connectionRequestCompleted(ManagedClientAsyncConnection conn) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] Connection allocated: " + conn);
        }
        this.connRequestCallback = null;
        try {
            this.managedConn = conn;
            if (this.closed) {
                conn.releaseConnection();
                return;
            }
            HttpRoute route = this.mainRequest.getRoute();
            if (!conn.isOpen()) {
                conn.open(route, this.localContext, (HttpParams) this.params);
            }
            conn.getContext().setAttribute("http.nio.exchange-handler", this);
            conn.requestOutput();
            this.routeEstablished = route.equals(conn.getRoute());
            if (!conn.isOpen()) {
                throw new ConnectionClosedException("Connection closed");
            }
        } catch (IOException ex) {
            failed(ex);
        } catch (RuntimeException runex) {
            failed(runex);
            throw runex;
        }
    }

    private synchronized void connectionRequestFailed(Exception ex) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] connection request failed");
        }
        this.connRequestCallback = null;
        try {
            this.resultCallback.failed(ex, this);
        } finally {
            close();
        }
    }

    private synchronized void connectionRequestCancelled() {
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] Connection request cancelled");
        }
        this.connRequestCallback = null;
        try {
            this.resultCallback.cancelled(this);
        } finally {
            close();
        }
    }

    private void requestConnection() {
        HttpRoute route = this.mainRequest.getRoute();
        if (this.log.isDebugEnabled()) {
            this.log.debug("[exchange: " + this.id + "] Request connection for " + route);
        }
        long connectTimeout = HttpConnectionParams.getConnectionTimeout((HttpParams) this.params);
        Object userToken = this.localContext.getAttribute("http.user-token");
        this.connRequestCallback = new InternalFutureCallback();
        this.connmgr.leaseConnection(route, userToken, connectTimeout, TimeUnit.MILLISECONDS, this.connRequestCallback);
    }

    public synchronized void endOfStream() {
        if (this.managedConn != null) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("[exchange: " + this.id + "] Unexpected end of data stream");
            }
            releaseConnection();
            if (this.connRequestCallback == null) {
                requestConnection();
            }
        }
    }

    protected HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
        HttpHost t = (target != null) ? target : (HttpHost) request.getParams().getParameter("http.default-host");

        if (t == null) {
            throw new IllegalStateException("Target host could not be resolved");
        }
        return this.routePlanner.determineRoute(t, request, context);
    }

    private RequestWrapper wrapRequest(HttpRequest request) throws ProtocolException {
        if (request instanceof HttpEntityEnclosingRequest) {
            return (RequestWrapper) new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest) request);
        }
        return new RequestWrapper(request);
    }

    protected void rewriteRequestURI(RequestWrapper request, HttpRoute route) throws ProtocolException {
        try {
            URI uri = request.getURI();
            if (route.getProxyHost() != null && !route.isTunnelled()) {

                if (!uri.isAbsolute()) {
                    HttpHost target = route.getTargetHost();
                    uri = URIUtils.rewriteURI(uri, target);
                    request.setURI(uri);
                }

            } else if (uri.isAbsolute()) {
                uri = URIUtils.rewriteURI(uri, null);
                request.setURI(uri);
            }

        } catch (URISyntaxException ex) {
            throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
        }
    }

    private AsyncSchemeRegistry getSchemeRegistry(HttpContext context) {
        AsyncSchemeRegistry reg = (AsyncSchemeRegistry) context.getAttribute("http.scheme-registry");

        if (reg == null) {
            reg = this.connmgr.getSchemeRegistry();
        }
        return reg;
    }

    private HttpRequest createConnectRequest(HttpRoute route) {
        HttpHost target = route.getTargetHost();
        String host = target.getHostName();
        int port = target.getPort();
        if (port < 0) {
            AsyncSchemeRegistry registry = getSchemeRegistry(this.localContext);
            AsyncScheme scheme = registry.getScheme(target.getSchemeName());
            port = scheme.getDefaultPort();
        }
        StringBuilder buffer = new StringBuilder(host.length() + 6);
        buffer.append(host);
        buffer.append(':');
        buffer.append(Integer.toString(port));
        ProtocolVersion ver = HttpProtocolParams.getVersion((HttpParams) this.params);
        return (HttpRequest) new BasicHttpRequest("CONNECT", buffer.toString(), ver);
    }

    private RoutedRequest handleResponse() throws HttpException {
        RoutedRequest followup = null;
        if (HttpClientParams.isAuthenticating((HttpParams) this.params)) {
            CredentialsProvider credsProvider = (CredentialsProvider) this.localContext.getAttribute("http.auth.credentials-provider");

            if (credsProvider != null) {
                followup = handleTargetChallenge(credsProvider);
                if (followup != null) {
                    return followup;
                }
                followup = handleProxyChallenge(credsProvider);
                if (followup != null) {
                    return followup;
                }
            }
        }
        if (HttpClientParams.isRedirecting((HttpParams) this.params)) {
            followup = handleRedirect();
            if (followup != null) {
                return followup;
            }
        }
        return null;
    }

    private RoutedRequest handleConnectResponse() throws HttpException {
        RoutedRequest followup = null;
        if (HttpClientParams.isAuthenticating((HttpParams) this.params)) {
            CredentialsProvider credsProvider = (CredentialsProvider) this.localContext.getAttribute("http.auth.credentials-provider");

            if (credsProvider != null) {
                followup = handleProxyChallenge(credsProvider);
                if (followup != null) {
                    return followup;
                }
            }
        }
        return null;
    }

    private RoutedRequest handleRedirect() throws HttpException {
        if (this.redirectStrategy.isRedirected((HttpRequest) this.currentRequest, this.currentResponse, this.localContext)) {

            HttpRoute route = this.mainRequest.getRoute();
            RequestWrapper request = this.mainRequest.getRequest();

            int maxRedirects = this.params.getIntParameter("http.protocol.max-redirects", 100);
            if (this.redirectCount >= maxRedirects) {
                throw new RedirectException("Maximum redirects (" + maxRedirects + ") exceeded");
            }

            this.redirectCount++;

            HttpUriRequest redirect = this.redirectStrategy.getRedirect((HttpRequest) this.currentRequest, this.currentResponse, this.localContext);

            HttpRequest orig = request.getOriginal();
            redirect.setHeaders(orig.getAllHeaders());

            URI uri = redirect.getURI();
            if (uri.getHost() == null) {
                throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri);
            }
            HttpHost newTarget = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());

            if (!route.getTargetHost().equals(newTarget)) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("[exchange: " + this.id + "] Resetting target auth state");
                }
                this.targetAuthState.reset();
                AuthScheme authScheme = this.proxyAuthState.getAuthScheme();
                if (authScheme != null && authScheme.isConnectionBased()) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("[exchange: " + this.id + "] Resetting proxy auth state");
                    }
                    this.proxyAuthState.reset();
                }
            }

            RequestWrapper newRequest = wrapRequest((HttpRequest) redirect);
            newRequest.setParams((HttpParams) this.params);

            HttpRoute newRoute = determineRoute(newTarget, (HttpRequest) newRequest, this.localContext);

            if (this.log.isDebugEnabled()) {
                this.log.debug("[exchange: " + this.id + "] Redirecting to '" + uri + "' via " + newRoute);
            }
            return new RoutedRequest(newRequest, newRoute);
        }
        return null;
    }

    private RoutedRequest handleTargetChallenge(CredentialsProvider credsProvider) throws HttpException {
        HttpRoute route = this.mainRequest.getRoute();
        HttpHost target = (HttpHost) this.localContext.getAttribute("http.target_host");

        if (target == null) {
            target = route.getTargetHost();
        }
        if (this.authenticator.isAuthenticationRequested(target, this.currentResponse, this.targetAuthStrategy, this.targetAuthState, this.localContext)) {

            if (this.authenticator.authenticate(target, this.currentResponse, this.targetAuthStrategy, this.targetAuthState, this.localContext)) {

                return this.mainRequest;
            }
            return null;
        }

        return null;
    }

    private RoutedRequest handleProxyChallenge(CredentialsProvider credsProvider) throws HttpException {
        HttpRoute route = this.mainRequest.getRoute();
        HttpHost proxy = route.getProxyHost();
        if (this.authenticator.isAuthenticationRequested(proxy, this.currentResponse, this.proxyAuthStrategy, this.proxyAuthState, this.localContext)) {

            if (this.authenticator.authenticate(proxy, this.currentResponse, this.proxyAuthStrategy, this.proxyAuthState, this.localContext)) {

                return this.mainRequest;
            }
            return null;
        }

        return null;
    }

    public HttpContext getContext() {
        return this.localContext;
    }

    public HttpProcessor getHttpProcessor() {
        return this.httppocessor;
    }

    public ConnectionReuseStrategy getConnectionReuseStrategy() {
        return this.reuseStrategy;
    }

    class InternalFutureCallback
            implements FutureCallback<ManagedClientAsyncConnection> {
        public void completed(ManagedClientAsyncConnection session) {
            DefaultAsyncRequestDirector.this.connectionRequestCompleted(session);
        }

        public void failed(Exception ex) {
            DefaultAsyncRequestDirector.this.connectionRequestFailed(ex);
        }

        public void cancelled() {
            DefaultAsyncRequestDirector.this.connectionRequestCancelled();
        }
    }
}

