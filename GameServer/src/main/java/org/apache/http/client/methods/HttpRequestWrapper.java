package org.apache.http.client.methods;

import org.apache.http.*;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

import java.net.URI;

@NotThreadSafe
public class HttpRequestWrapper
        extends AbstractHttpMessage
        implements HttpUriRequest {
    private final HttpRequest original;
    private final HttpHost target;
    private final String method;
    private ProtocolVersion version;
    private URI uri;

    private HttpRequestWrapper(HttpRequest request, HttpHost target) {
        this.original = (HttpRequest) Args.notNull(request, "HTTP request");
        this.target = target;
        this.version = this.original.getRequestLine().getProtocolVersion();
        this.method = this.original.getRequestLine().getMethod();
        if (request instanceof HttpUriRequest) {
            this.uri = ((HttpUriRequest) request).getURI();
        } else {
            this.uri = null;
        }
        setHeaders(request.getAllHeaders());
    }

    public static HttpRequestWrapper wrap(HttpRequest request) {
        return wrap(request, (HttpHost) null);
    }

    public static HttpRequestWrapper wrap(HttpRequest request, HttpHost target) {
        Args.notNull(request, "HTTP request");
        if (request instanceof HttpEntityEnclosingRequest) {
            return new HttpEntityEnclosingRequestWrapper((HttpEntityEnclosingRequest) request, target);
        }
        return new HttpRequestWrapper(request, target);
    }

    public ProtocolVersion getProtocolVersion() {
        return (this.version != null) ? this.version : this.original.getProtocolVersion();
    }

    public void setProtocolVersion(ProtocolVersion version) {
        this.version = version;
    }

    public URI getURI() {
        return this.uri;
    }

    public void setURI(URI uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return this.method;
    }

    public void abort() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean isAborted() {
        return false;
    }

    public RequestLine getRequestLine() {
        String requestUri = null;
        if (this.uri != null) {
            requestUri = this.uri.toASCIIString();
        } else {
            requestUri = this.original.getRequestLine().getUri();
        }
        if (requestUri == null || requestUri.isEmpty()) {
            requestUri = "/";
        }
        return (RequestLine) new BasicRequestLine(this.method, requestUri, getProtocolVersion());
    }

    public HttpRequest getOriginal() {
        return this.original;
    }

    public HttpHost getTarget() {
        return this.target;
    }

    public String toString() {
        return getRequestLine() + " " + this.headergroup;
    }

    @Deprecated
    public HttpParams getParams() {
        if (this.params == null) {
            this.params = this.original.getParams().copy();
        }
        return this.params;
    }

    static class HttpEntityEnclosingRequestWrapper
            extends HttpRequestWrapper
            implements HttpEntityEnclosingRequest {
        private HttpEntity entity;

        HttpEntityEnclosingRequestWrapper(HttpEntityEnclosingRequest request, HttpHost target) {
            super((HttpRequest) request, target);
            this.entity = request.getEntity();
        }

        public HttpEntity getEntity() {
            return this.entity;
        }

        public void setEntity(HttpEntity entity) {
            this.entity = entity;
        }

        public boolean expectContinue() {
            Header expect = getFirstHeader("Expect");
            return (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
        }
    }
}

