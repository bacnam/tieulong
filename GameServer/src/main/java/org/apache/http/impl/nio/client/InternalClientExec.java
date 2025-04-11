package org.apache.http.impl.nio.client;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;

import java.io.IOException;

interface InternalClientExec {
    void prepare(InternalState paramInternalState, HttpHost paramHttpHost, HttpRequest paramHttpRequest) throws IOException, HttpException;

    HttpRequest generateRequest(InternalState paramInternalState, InternalConnManager paramInternalConnManager) throws IOException, HttpException;

    void produceContent(InternalState paramInternalState, ContentEncoder paramContentEncoder, IOControl paramIOControl) throws IOException;

    void requestCompleted(InternalState paramInternalState);

    void responseReceived(InternalState paramInternalState, HttpResponse paramHttpResponse) throws IOException, HttpException;

    void consumeContent(InternalState paramInternalState, ContentDecoder paramContentDecoder, IOControl paramIOControl) throws IOException;

    void responseCompleted(InternalState paramInternalState, InternalConnManager paramInternalConnManager) throws IOException, HttpException;
}

