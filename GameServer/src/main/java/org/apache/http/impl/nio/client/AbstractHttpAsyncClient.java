package org.apache.http.impl.nio.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.*;
import org.apache.http.impl.nio.DefaultHttpClientIODispatch;
import org.apache.http.impl.nio.conn.DefaultHttpAsyncRoutePlanner;
import org.apache.http.impl.nio.conn.PoolingClientAsyncConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.NHttpClientEventHandler;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.conn.ClientAsyncConnectionManager;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutionHandler;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.IOReactorStatus;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.*;

import java.io.IOException;
import java.net.URI;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

@Deprecated
public abstract class AbstractHttpAsyncClient
        implements HttpAsyncClient {
    private final Log log = LogFactory.getLog(getClass());

    private final ClientAsyncConnectionManager connmgr;

    private final Queue<HttpAsyncRequestExecutionHandler<?>> queue;

    private Thread reactorThread;
    private BasicHttpProcessor mutableProcessor;
    private ImmutableHttpProcessor protocolProcessor;
    private ConnectionReuseStrategy reuseStrategy;
    private ConnectionKeepAliveStrategy keepAliveStrategy;
    private RedirectStrategy redirectStrategy;
    private CookieSpecRegistry supportedCookieSpecs;
    private CookieStore cookieStore;
    private AuthSchemeRegistry supportedAuthSchemes;
    private AuthenticationStrategy targetAuthStrategy;
    private AuthenticationStrategy proxyAuthStrategy;
    private CredentialsProvider credsProvider;
    private HttpRoutePlanner routePlanner;
    private UserTokenHandler userTokenHandler;
    private HttpParams params;
    private volatile boolean terminated;

    protected AbstractHttpAsyncClient(ClientAsyncConnectionManager connmgr) {
        this.connmgr = connmgr;
        this.queue = new ConcurrentLinkedQueue<HttpAsyncRequestExecutionHandler<?>>();
    }

    protected AbstractHttpAsyncClient(IOReactorConfig config) throws IOReactorException {
        DefaultConnectingIOReactor defaultioreactor = new DefaultConnectingIOReactor(config);
        defaultioreactor.setExceptionHandler(new InternalIOReactorExceptionHandler(this.log));
        this.connmgr = (ClientAsyncConnectionManager) new PoolingClientAsyncConnectionManager((ConnectingIOReactor) defaultioreactor);
        this.queue = new ConcurrentLinkedQueue<HttpAsyncRequestExecutionHandler<?>>();
    }

    protected HttpContext createHttpContext() {
        BasicHttpContext basicHttpContext = new BasicHttpContext();
        basicHttpContext.setAttribute("http.scheme-registry", getConnectionManager().getSchemeRegistry());

        basicHttpContext.setAttribute("http.authscheme-registry", getAuthSchemes());

        basicHttpContext.setAttribute("http.cookiespec-registry", getCookieSpecs());

        basicHttpContext.setAttribute("http.cookie-store", getCookieStore());

        basicHttpContext.setAttribute("http.auth.credentials-provider", getCredentialsProvider());

        return (HttpContext) basicHttpContext;
    }

    protected ConnectionReuseStrategy createConnectionReuseStrategy() {
        return (ConnectionReuseStrategy) new DefaultConnectionReuseStrategy();
    }

    protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
        return (ConnectionKeepAliveStrategy) new DefaultConnectionKeepAliveStrategy();
    }

    protected AuthSchemeRegistry createAuthSchemeRegistry() {
        AuthSchemeRegistry registry = new AuthSchemeRegistry();
        registry.register("Basic", (AuthSchemeFactory) new BasicSchemeFactory());

        registry.register("Digest", (AuthSchemeFactory) new DigestSchemeFactory());

        registry.register("NTLM", (AuthSchemeFactory) new NTLMSchemeFactory());

        registry.register("negotiate", (AuthSchemeFactory) new SPNegoSchemeFactory());

        registry.register("Kerberos", (AuthSchemeFactory) new KerberosSchemeFactory());

        return registry;
    }

    protected CookieSpecRegistry createCookieSpecRegistry() {
        CookieSpecRegistry registry = new CookieSpecRegistry();
        registry.register("best-match", (CookieSpecFactory) new BestMatchSpecFactory());

        registry.register("compatibility", (CookieSpecFactory) new BrowserCompatSpecFactory());

        registry.register("netscape", (CookieSpecFactory) new NetscapeDraftSpecFactory());

        registry.register("rfc2109", (CookieSpecFactory) new RFC2109SpecFactory());

        registry.register("rfc2965", (CookieSpecFactory) new RFC2965SpecFactory());

        registry.register("ignoreCookies", (CookieSpecFactory) new IgnoreSpecFactory());

        return registry;
    }

    protected AuthenticationStrategy createTargetAuthenticationStrategy() {
        return (AuthenticationStrategy) new TargetAuthenticationStrategy();
    }

    protected AuthenticationStrategy createProxyAuthenticationStrategy() {
        return (AuthenticationStrategy) new ProxyAuthenticationStrategy();
    }

    protected CookieStore createCookieStore() {
        return (CookieStore) new BasicCookieStore();
    }

    protected CredentialsProvider createCredentialsProvider() {
        return (CredentialsProvider) new BasicCredentialsProvider();
    }

    protected HttpRoutePlanner createHttpRoutePlanner() {
        return (HttpRoutePlanner) new DefaultHttpAsyncRoutePlanner(getConnectionManager().getSchemeRegistry());
    }

    protected UserTokenHandler createUserTokenHandler() {
        return (UserTokenHandler) new DefaultUserTokenHandler();
    }

    public final synchronized HttpParams getParams() {
        if (this.params == null) {
            this.params = createHttpParams();
        }
        return this.params;
    }

    public synchronized void setParams(HttpParams params) {
        this.params = params;
    }

    public synchronized ClientAsyncConnectionManager getConnectionManager() {
        return this.connmgr;
    }

    public final synchronized ConnectionReuseStrategy getConnectionReuseStrategy() {
        if (this.reuseStrategy == null) {
            this.reuseStrategy = createConnectionReuseStrategy();
        }
        return this.reuseStrategy;
    }

    public synchronized void setReuseStrategy(ConnectionReuseStrategy reuseStrategy) {
        this.reuseStrategy = reuseStrategy;
    }

    public final synchronized ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
        if (this.keepAliveStrategy == null) {
            this.keepAliveStrategy = createConnectionKeepAliveStrategy();
        }
        return this.keepAliveStrategy;
    }

    public synchronized void setKeepAliveStrategy(ConnectionKeepAliveStrategy keepAliveStrategy) {
        this.keepAliveStrategy = keepAliveStrategy;
    }

    public final synchronized RedirectStrategy getRedirectStrategy() {
        if (this.redirectStrategy == null) {
            this.redirectStrategy = (RedirectStrategy) new DefaultRedirectStrategy();
        }
        return this.redirectStrategy;
    }

    public synchronized void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    public final synchronized AuthSchemeRegistry getAuthSchemes() {
        if (this.supportedAuthSchemes == null) {
            this.supportedAuthSchemes = createAuthSchemeRegistry();
        }
        return this.supportedAuthSchemes;
    }

    public synchronized void setAuthSchemes(AuthSchemeRegistry authSchemeRegistry) {
        this.supportedAuthSchemes = authSchemeRegistry;
    }

    public final synchronized CookieSpecRegistry getCookieSpecs() {
        if (this.supportedCookieSpecs == null) {
            this.supportedCookieSpecs = createCookieSpecRegistry();
        }
        return this.supportedCookieSpecs;
    }

    public synchronized void setCookieSpecs(CookieSpecRegistry cookieSpecRegistry) {
        this.supportedCookieSpecs = cookieSpecRegistry;
    }

    public final synchronized AuthenticationStrategy getTargetAuthenticationStrategy() {
        if (this.targetAuthStrategy == null) {
            this.targetAuthStrategy = createTargetAuthenticationStrategy();
        }
        return this.targetAuthStrategy;
    }

    public synchronized void setTargetAuthenticationStrategy(AuthenticationStrategy targetAuthStrategy) {
        this.targetAuthStrategy = targetAuthStrategy;
    }

    public final synchronized AuthenticationStrategy getProxyAuthenticationStrategy() {
        if (this.proxyAuthStrategy == null) {
            this.proxyAuthStrategy = createProxyAuthenticationStrategy();
        }
        return this.proxyAuthStrategy;
    }

    public synchronized void setProxyAuthenticationStrategy(AuthenticationStrategy proxyAuthStrategy) {
        this.proxyAuthStrategy = proxyAuthStrategy;
    }

    public final synchronized CookieStore getCookieStore() {
        if (this.cookieStore == null) {
            this.cookieStore = createCookieStore();
        }
        return this.cookieStore;
    }

    public synchronized void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public final synchronized CredentialsProvider getCredentialsProvider() {
        if (this.credsProvider == null) {
            this.credsProvider = createCredentialsProvider();
        }
        return this.credsProvider;
    }

    public synchronized void setCredentialsProvider(CredentialsProvider credsProvider) {
        this.credsProvider = credsProvider;
    }

    public final synchronized HttpRoutePlanner getRoutePlanner() {
        if (this.routePlanner == null) {
            this.routePlanner = createHttpRoutePlanner();
        }
        return this.routePlanner;
    }

    public synchronized void setRoutePlanner(HttpRoutePlanner routePlanner) {
        this.routePlanner = routePlanner;
    }

    public final synchronized UserTokenHandler getUserTokenHandler() {
        if (this.userTokenHandler == null) {
            this.userTokenHandler = createUserTokenHandler();
        }
        return this.userTokenHandler;
    }

    public synchronized void setUserTokenHandler(UserTokenHandler userTokenHandler) {
        this.userTokenHandler = userTokenHandler;
    }

    protected final synchronized BasicHttpProcessor getHttpProcessor() {
        if (this.mutableProcessor == null) {
            this.mutableProcessor = createHttpProcessor();
        }
        return this.mutableProcessor;
    }

    private final synchronized HttpProcessor getProtocolProcessor() {
        if (this.protocolProcessor == null) {

            BasicHttpProcessor proc = getHttpProcessor();

            int reqc = proc.getRequestInterceptorCount();
            HttpRequestInterceptor[] reqinterceptors = new HttpRequestInterceptor[reqc];
            for (int i = 0; i < reqc; i++) {
                reqinterceptors[i] = proc.getRequestInterceptor(i);
            }
            int resc = proc.getResponseInterceptorCount();
            HttpResponseInterceptor[] resinterceptors = new HttpResponseInterceptor[resc];
            for (int j = 0; j < resc; j++) {
                resinterceptors[j] = proc.getResponseInterceptor(j);
            }
            this.protocolProcessor = new ImmutableHttpProcessor(reqinterceptors, resinterceptors);
        }
        return (HttpProcessor) this.protocolProcessor;
    }

    public synchronized int getResponseInterceptorCount() {
        return getHttpProcessor().getResponseInterceptorCount();
    }

    public synchronized HttpResponseInterceptor getResponseInterceptor(int index) {
        return getHttpProcessor().getResponseInterceptor(index);
    }

    public synchronized HttpRequestInterceptor getRequestInterceptor(int index) {
        return getHttpProcessor().getRequestInterceptor(index);
    }

    public synchronized int getRequestInterceptorCount() {
        return getHttpProcessor().getRequestInterceptorCount();
    }

    public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp) {
        getHttpProcessor().addInterceptor(itcp);
        this.protocolProcessor = null;
    }

    public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp, int index) {
        getHttpProcessor().addInterceptor(itcp, index);
        this.protocolProcessor = null;
    }

    public synchronized void clearResponseInterceptors() {
        getHttpProcessor().clearResponseInterceptors();
        this.protocolProcessor = null;
    }

    public synchronized void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz) {
        getHttpProcessor().removeResponseInterceptorByClass(clazz);
        this.protocolProcessor = null;
    }

    public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp) {
        getHttpProcessor().addInterceptor(itcp);
        this.protocolProcessor = null;
    }

    public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp, int index) {
        getHttpProcessor().addInterceptor(itcp, index);
        this.protocolProcessor = null;
    }

    public synchronized void clearRequestInterceptors() {
        getHttpProcessor().clearRequestInterceptors();
        this.protocolProcessor = null;
    }

    public synchronized void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz) {
        getHttpProcessor().removeRequestInterceptorByClass(clazz);
        this.protocolProcessor = null;
    }

    private void doExecute() {
        LoggingAsyncRequestExecutor handler = new LoggingAsyncRequestExecutor();
        try {
            DefaultHttpClientIODispatch defaultHttpClientIODispatch = new DefaultHttpClientIODispatch((NHttpClientEventHandler) handler, getParams());
            this.connmgr.execute((IOEventDispatch) defaultHttpClientIODispatch);
        } catch (Exception ex) {
            this.log.error("I/O reactor terminated abnormally", ex);
        } finally {
            this.terminated = true;
            while (!this.queue.isEmpty()) {
                HttpAsyncRequestExecutionHandler<?> exchangeHandler = this.queue.remove();
                exchangeHandler.cancel();
            }
        }
    }

    public IOReactorStatus getStatus() {
        return this.connmgr.getStatus();
    }

    public synchronized void start() {
        this.reactorThread = new Thread() {
            public void run() {
                AbstractHttpAsyncClient.this.doExecute();
            }
        };

        this.reactorThread.start();
    }

    public void shutdown() throws InterruptedException {
        try {
            this.connmgr.shutdown(5000L);
        } catch (IOException ex) {
            this.log.error("I/O error shutting down", ex);
        }
        if (this.reactorThread != null) {
            this.reactorThread.join();
        }
    }

    public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
        DefaultAsyncRequestDirector<T> httpexchange;
        if (this.terminated) {
            throw new IllegalStateException("Client has been shut down");
        }
        BasicFuture<T> future = new BasicFuture(callback);
        ResultCallback<T> resultCallback = new DefaultResultCallback<T>(future, this.queue);

        synchronized (this) {
            DefaultedHttpContext defaultedHttpContext;
            HttpContext defaultContext = createHttpContext();

            if (context == null) {
                HttpContext execContext = defaultContext;
            } else {
                defaultedHttpContext = new DefaultedHttpContext(context, defaultContext);
            }
            httpexchange = new DefaultAsyncRequestDirector<T>(this.log, requestProducer, responseConsumer, (HttpContext) defaultedHttpContext, resultCallback, this.connmgr, getProtocolProcessor(), getRoutePlanner(), getConnectionReuseStrategy(), getConnectionKeepAliveStrategy(), getRedirectStrategy(), getTargetAuthenticationStrategy(), getProxyAuthenticationStrategy(), getUserTokenHandler(), getParams());
        }

        this.queue.add(httpexchange);
        httpexchange.start();
        return (Future<T>) future;
    }

    public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback) {
        return execute(requestProducer, responseConsumer, (HttpContext) new BasicHttpContext(), callback);
    }

    public Future<HttpResponse> execute(HttpHost target, HttpRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
        return execute(HttpAsyncMethods.create(target, request), HttpAsyncMethods.createConsumer(), context, callback);
    }

    public Future<HttpResponse> execute(HttpHost target, HttpRequest request, FutureCallback<HttpResponse> callback) {
        return execute(target, request, (HttpContext) new BasicHttpContext(), callback);
    }

    public Future<HttpResponse> execute(HttpUriRequest request, FutureCallback<HttpResponse> callback) {
        return execute(request, (HttpContext) new BasicHttpContext(), callback);
    }

    public Future<HttpResponse> execute(HttpUriRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
        HttpHost target;
        try {
            target = determineTarget(request);
        } catch (ClientProtocolException ex) {
            BasicFuture<HttpResponse> future = new BasicFuture(callback);
            future.failed((Exception) ex);
            return (Future<HttpResponse>) future;
        }
        return execute(target, (HttpRequest) request, context, callback);
    }

    private HttpHost determineTarget(HttpUriRequest request) throws ClientProtocolException {
        HttpHost target = null;

        URI requestURI = request.getURI();
        if (requestURI.isAbsolute()) {
            target = URIUtils.extractHost(requestURI);
            if (target == null) {
                throw new ClientProtocolException("URI does not specify a valid host name: " + requestURI);
            }
        }

        return target;
    }

    protected abstract HttpParams createHttpParams();

    protected abstract BasicHttpProcessor createHttpProcessor();
}

