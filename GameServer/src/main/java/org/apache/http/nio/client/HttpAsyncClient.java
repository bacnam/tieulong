package org.apache.http.nio.client;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.Future;

public interface HttpAsyncClient {
    <T> Future<T> execute(HttpAsyncRequestProducer paramHttpAsyncRequestProducer, HttpAsyncResponseConsumer<T> paramHttpAsyncResponseConsumer, HttpContext paramHttpContext, FutureCallback<T> paramFutureCallback);

    <T> Future<T> execute(HttpAsyncRequestProducer paramHttpAsyncRequestProducer, HttpAsyncResponseConsumer<T> paramHttpAsyncResponseConsumer, FutureCallback<T> paramFutureCallback);

    Future<HttpResponse> execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext, FutureCallback<HttpResponse> paramFutureCallback);

    Future<HttpResponse> execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, FutureCallback<HttpResponse> paramFutureCallback);

    Future<HttpResponse> execute(HttpUriRequest paramHttpUriRequest, HttpContext paramHttpContext, FutureCallback<HttpResponse> paramFutureCallback);

    Future<HttpResponse> execute(HttpUriRequest paramHttpUriRequest, FutureCallback<HttpResponse> paramFutureCallback);
}

