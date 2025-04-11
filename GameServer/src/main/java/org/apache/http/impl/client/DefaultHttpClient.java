package org.apache.http.impl.client;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.protocol.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.*;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.util.VersionInfo;

@Deprecated
@ThreadSafe
public class DefaultHttpClient
        extends AbstractHttpClient {
    public DefaultHttpClient(ClientConnectionManager conman, HttpParams params) {
        super(conman, params);
    }

    public DefaultHttpClient(ClientConnectionManager conman) {
        super(conman, null);
    }

    public DefaultHttpClient(HttpParams params) {
        super(null, params);
    }

    public DefaultHttpClient() {
        super(null, null);
    }

    public static void setDefaultHttpParams(HttpParams params) {
        HttpProtocolParams.setVersion(params, (ProtocolVersion) HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEF_CONTENT_CHARSET.name());
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpProtocolParams.setUserAgent(params, VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", DefaultHttpClient.class));
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

