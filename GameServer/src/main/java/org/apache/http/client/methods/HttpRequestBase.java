package org.apache.http.client.methods;

import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpProtocolParams;

import java.net.URI;

@NotThreadSafe
public abstract class HttpRequestBase
        extends AbstractExecutionAwareRequest
        implements HttpUriRequest, Configurable {
    private ProtocolVersion version;
    private URI uri;
    private RequestConfig config;

    public abstract String getMethod();

    public ProtocolVersion getProtocolVersion() {
        return (this.version != null) ? this.version : HttpProtocolParams.getVersion(getParams());
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

    public RequestLine getRequestLine() {
        String method = getMethod();
        ProtocolVersion ver = getProtocolVersion();
        URI uriCopy = getURI();
        String uritext = null;
        if (uriCopy != null) {
            uritext = uriCopy.toASCIIString();
        }
        if (uritext == null || uritext.isEmpty()) {
            uritext = "/";
        }
        return (RequestLine) new BasicRequestLine(method, uritext, ver);
    }

    public RequestConfig getConfig() {
        return this.config;
    }

    public void setConfig(RequestConfig config) {
        this.config = config;
    }

    public void started() {
    }

    public void releaseConnection() {
        reset();
    }

    public String toString() {
        return getMethod() + " " + getURI() + " " + getProtocolVersion();
    }
}

