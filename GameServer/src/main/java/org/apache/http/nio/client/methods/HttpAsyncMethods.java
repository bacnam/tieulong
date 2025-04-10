package org.apache.http.nio.client.methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.HttpAsyncContentProducer;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.util.Args;

public final class HttpAsyncMethods
{
public static HttpAsyncRequestProducer create(HttpHost target, HttpRequest request) {
Args.notNull(target, "HTTP host");
Args.notNull(request, "HTTP request");
return (HttpAsyncRequestProducer)new RequestProducerImpl(target, request);
}

public static HttpAsyncRequestProducer create(HttpUriRequest request) {
Args.notNull(request, "HTTP request");
HttpHost target = URIUtils.extractHost(request.getURI());
return (HttpAsyncRequestProducer)new RequestProducerImpl(target, (HttpRequest)request);
}

public static HttpAsyncRequestProducer createGet(URI requestURI) {
return create((HttpUriRequest)new HttpGet(requestURI));
}

public static HttpAsyncRequestProducer createGet(String requestURI) {
return create((HttpUriRequest)new HttpGet(URI.create(requestURI)));
}

public static HttpAsyncRequestProducer createHead(URI requestURI) {
return create((HttpUriRequest)new HttpGet(requestURI));
}

public static HttpAsyncRequestProducer createHead(String requestURI) {
return create((HttpUriRequest)new HttpGet(URI.create(requestURI)));
}

public static HttpAsyncRequestProducer createDelete(URI requestURI) {
return create((HttpUriRequest)new HttpDelete(requestURI));
}

public static HttpAsyncRequestProducer createDelete(String requestURI) {
return create((HttpUriRequest)new HttpDelete(URI.create(requestURI)));
}

public static HttpAsyncRequestProducer createOptions(URI requestURI) {
return create((HttpUriRequest)new HttpOptions(requestURI));
}

public static HttpAsyncRequestProducer createOptions(String requestURI) {
return create((HttpUriRequest)new HttpOptions(URI.create(requestURI)));
}

public static HttpAsyncRequestProducer createTrace(URI requestURI) {
return create((HttpUriRequest)new HttpTrace(requestURI));
}

public static HttpAsyncRequestProducer createTrace(String requestURI) {
return create((HttpUriRequest)new HttpTrace(URI.create(requestURI)));
}

public static HttpAsyncRequestProducer createPost(URI requestURI, String content, ContentType contentType) throws UnsupportedEncodingException {
HttpPost httppost = new HttpPost(requestURI);
NStringEntity entity = new NStringEntity(content, contentType);
httppost.setEntity((HttpEntity)entity);
HttpHost target = URIUtils.extractHost(requestURI);
return (HttpAsyncRequestProducer)new RequestProducerImpl(target, (HttpEntityEnclosingRequest)httppost, (HttpAsyncContentProducer)entity);
}

public static HttpAsyncRequestProducer createPost(String requestURI, String content, ContentType contentType) throws UnsupportedEncodingException {
return createPost(URI.create(requestURI), content, contentType);
}

public static HttpAsyncRequestProducer createPost(URI requestURI, byte[] content, ContentType contentType) {
HttpPost httppost = new HttpPost(requestURI);
NByteArrayEntity entity = new NByteArrayEntity(content, contentType);
HttpHost target = URIUtils.extractHost(requestURI);
return (HttpAsyncRequestProducer)new RequestProducerImpl(target, (HttpEntityEnclosingRequest)httppost, (HttpAsyncContentProducer)entity);
}

public static HttpAsyncRequestProducer createPost(String requestURI, byte[] content, ContentType contentType) {
return createPost(URI.create(requestURI), content, contentType);
}

public static HttpAsyncRequestProducer createPut(URI requestURI, String content, ContentType contentType) throws UnsupportedEncodingException {
HttpPut httpput = new HttpPut(requestURI);
NStringEntity entity = new NStringEntity(content, contentType);
httpput.setEntity((HttpEntity)entity);
HttpHost target = URIUtils.extractHost(requestURI);
return (HttpAsyncRequestProducer)new RequestProducerImpl(target, (HttpEntityEnclosingRequest)httpput, (HttpAsyncContentProducer)entity);
}

public static HttpAsyncRequestProducer createPut(String requestURI, String content, ContentType contentType) throws UnsupportedEncodingException {
return createPut(URI.create(requestURI), content, contentType);
}

public static HttpAsyncRequestProducer createPut(URI requestURI, byte[] content, ContentType contentType) {
HttpPut httpput = new HttpPut(requestURI);
NByteArrayEntity entity = new NByteArrayEntity(content, contentType);
HttpHost target = URIUtils.extractHost(requestURI);
return (HttpAsyncRequestProducer)new RequestProducerImpl(target, (HttpEntityEnclosingRequest)httpput, (HttpAsyncContentProducer)entity);
}

public static HttpAsyncRequestProducer createPut(String requestURI, byte[] content, ContentType contentType) {
return createPut(URI.create(requestURI), content, contentType);
}

public static HttpAsyncRequestProducer createZeroCopyPost(URI requestURI, File content, ContentType contentType) throws FileNotFoundException {
return new ZeroCopyPost(requestURI, content, contentType);
}

public static HttpAsyncRequestProducer createZeroCopyPost(String requestURI, File content, ContentType contentType) throws FileNotFoundException {
return new ZeroCopyPost(URI.create(requestURI), content, contentType);
}

public static HttpAsyncRequestProducer createZeroCopyPut(URI requestURI, File content, ContentType contentType) throws FileNotFoundException {
return new ZeroCopyPut(requestURI, content, contentType);
}

public static HttpAsyncRequestProducer createZeroCopyPut(String requestURI, File content, ContentType contentType) throws FileNotFoundException {
return new ZeroCopyPut(URI.create(requestURI), content, contentType);
}

public static HttpAsyncResponseConsumer<HttpResponse> createConsumer() {
return (HttpAsyncResponseConsumer<HttpResponse>)new BasicAsyncResponseConsumer();
}

public static HttpAsyncResponseConsumer<HttpResponse> createZeroCopyConsumer(File file) throws FileNotFoundException {
return (HttpAsyncResponseConsumer<HttpResponse>)new ZeroCopyConsumer<HttpResponse>(file)
{

protected HttpResponse process(HttpResponse response, File file, ContentType contentType)
{
return response;
}
};
}

static class RequestProducerImpl
extends BasicAsyncRequestProducer
{
protected RequestProducerImpl(HttpHost target, HttpEntityEnclosingRequest request, HttpAsyncContentProducer producer) {
super(target, request, producer);
}

public RequestProducerImpl(HttpHost target, HttpRequest request) {
super(target, request);
}
}
}

