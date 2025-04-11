package org.apache.http.impl.nio.client;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.protocol.*;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.ClientAsyncConnectionManager;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.*;
import org.apache.http.protocol.RequestExpectContinue;

@Deprecated
public class DefaultHttpAsyncClient
        extends AbstractHttpAsyncClient {
    public DefaultHttpAsyncClient(ClientAsyncConnectionManager connmgr) {
        super(connmgr);
    }

    public DefaultHttpAsyncClient(IOReactorConfig config) throws IOReactorException {
        super(config);
    }

    public DefaultHttpAsyncClient() throws IOReactorException {
        super(new IOReactorConfig());
    }

    public static void setDefaultHttpParams(HttpParams params) {
        HttpProtocolParams.setVersion(params, (ProtocolVersion) HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEF_CONTENT_CHARSET.name());
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpProtocolParams.setUserAgent(params, HttpAsyncClientBuilder.DEFAULT_USER_AGENT);
    }

    protected HttpParams createHttpParams() {
        SyncBasicHttpParams syncBasicHttpParams = new SyncBasicHttpParams();
        setDefaultHttpParams((HttpParams) syncBasicHttpParams);
        return (HttpParams) syncBasicHttpParams;
    }

    protected BasicHttpProcessor createHttpProcessor() {
        BasicHttpProcessor httpproc = new BasicHttpProcessor();
        httpproc.addInterceptor((HttpRequestInterceptor) new RequestDefaultHeaders());

        httpproc.addInterceptor((HttpRequestInterceptor) new RequestContent());
        httpproc.addInterceptor((HttpRequestInterceptor) new RequestTargetHost());

        httpproc.addInterceptor((HttpRequestInterceptor) new RequestClientConnControl());
        httpproc.addInterceptor((HttpRequestInterceptor) new RequestUserAgent());
        httpproc.addInterceptor((HttpRequestInterceptor) new RequestExpectContinue());

        httpproc.addInterceptor((HttpRequestInterceptor) new RequestAddCookies());
        httpproc.addInterceptor((HttpResponseInterceptor) new ResponseProcessCookies());

        httpproc.addInterceptor((HttpRequestInterceptor) new RequestAuthCache());
        httpproc.addInterceptor((HttpRequestInterceptor) new RequestTargetAuthentication());
        httpproc.addInterceptor((HttpRequestInterceptor) new RequestProxyAuthentication());
        return httpproc;
    }
}

