package org.apache.http.nio.client.methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.protocol.HttpContext;

public class ZeroCopyPost
extends BaseZeroCopyRequestProducer
{
public ZeroCopyPost(URI requestURI, File content, ContentType contentType) throws FileNotFoundException {
super(requestURI, content, contentType);
}

public ZeroCopyPost(String requestURI, File content, ContentType contentType) throws FileNotFoundException {
super(URI.create(requestURI), content, contentType);
}

protected HttpEntityEnclosingRequest createRequest(URI requestURI, HttpEntity entity) {
HttpPost httppost = new HttpPost(requestURI);
httppost.setEntity(entity);
return (HttpEntityEnclosingRequest)httppost;
}
}

