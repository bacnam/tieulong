package com.zhonglian.server.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

public class HttpAsyncClient {
    static CloseableHttpAsyncClient httpclient = null;

    public static void init() {
        if (httpclient == null) {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
            httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
            httpclient.start();
        }
    }

    public static void start(HttpRequestBase request, FutureCallback<HttpResponse> response) {
        init();
        httpclient.execute((HttpUriRequest) request, response);
    }

    public static void startHttpGet(String request, FutureCallback<HttpResponse> response) {
        init();
        httpclient.execute((HttpUriRequest) new HttpGet(request), response);
    }

    public static void startHttpGet(String request, IResponseHandler response) {
        init();
        httpclient.execute((HttpUriRequest) new HttpGet(request), response);
    }

    public static void startHttpPost(HttpPost httpRequest, IResponseHandler response) {
        init();
        httpclient.execute((HttpUriRequest) httpRequest, response);
    }
}

