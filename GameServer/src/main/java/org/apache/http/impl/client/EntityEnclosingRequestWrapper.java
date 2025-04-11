package org.apache.http.impl.client;

import org.apache.http.*;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Deprecated
@NotThreadSafe
public class EntityEnclosingRequestWrapper
        extends RequestWrapper
        implements HttpEntityEnclosingRequest {
    private HttpEntity entity;
    private boolean consumed;

    public EntityEnclosingRequestWrapper(HttpEntityEnclosingRequest request) throws ProtocolException {
        super((HttpRequest) request);
        setEntity(request.getEntity());
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public void setEntity(HttpEntity entity) {
        this.entity = (entity != null) ? (HttpEntity) new EntityWrapper(entity) : null;
        this.consumed = false;
    }

    public boolean expectContinue() {
        Header expect = getFirstHeader("Expect");
        return (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
    }

    public boolean isRepeatable() {
        return (this.entity == null || this.entity.isRepeatable() || !this.consumed);
    }

    class EntityWrapper
            extends HttpEntityWrapper {
        EntityWrapper(HttpEntity entity) {
            super(entity);
        }

        public void consumeContent() throws IOException {
            EntityEnclosingRequestWrapper.this.consumed = true;
            super.consumeContent();
        }

        public InputStream getContent() throws IOException {
            EntityEnclosingRequestWrapper.this.consumed = true;
            return super.getContent();
        }

        public void writeTo(OutputStream outstream) throws IOException {
            EntityEnclosingRequestWrapper.this.consumed = true;
            super.writeTo(outstream);
        }
    }
}

