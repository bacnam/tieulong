package org.apache.http.nio.protocol;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.entity.EntityAsyncContentProducer;
import org.apache.http.nio.entity.HttpAsyncContentProducer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

import java.io.IOException;

public class BasicAsyncRequestProducer
        implements HttpAsyncRequestProducer {
    private final HttpHost target;
    private final HttpRequest request;
    private final HttpAsyncContentProducer producer;

    protected BasicAsyncRequestProducer(HttpHost target, HttpEntityEnclosingRequest request, HttpAsyncContentProducer producer) {
        Args.notNull(target, "HTTP host");
        Args.notNull(request, "HTTP request");
        Args.notNull(producer, "HTTP content producer");
        this.target = target;
        this.request = (HttpRequest) request;
        this.producer = producer;
    }

    public BasicAsyncRequestProducer(HttpHost target, HttpRequest request) {
        Args.notNull(target, "HTTP host");
        Args.notNull(request, "HTTP request");
        this.target = target;
        this.request = request;
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            if (entity != null) {
                if (entity instanceof HttpAsyncContentProducer) {
                    this.producer = (HttpAsyncContentProducer) entity;
                } else {
                    this.producer = (HttpAsyncContentProducer) new EntityAsyncContentProducer(entity);
                }
            } else {
                this.producer = null;
            }
        } else {
            this.producer = null;
        }
    }

    public HttpRequest generateRequest() {
        return this.request;
    }

    public HttpHost getTarget() {
        return this.target;
    }

    public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
        if (this.producer != null) {
            this.producer.produceContent(encoder, ioctrl);
            if (encoder.isCompleted()) {
                this.producer.close();
            }
        }
    }

    public void requestCompleted(HttpContext context) {
    }

    public void failed(Exception ex) {
    }

    public boolean isRepeatable() {
        return (this.producer == null || this.producer.isRepeatable());
    }

    public void resetRequest() throws IOException {
        if (this.producer != null) {
            this.producer.close();
        }
    }

    public void close() throws IOException {
        if (this.producer != null) {
            this.producer.close();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.target);
        sb.append(' ');
        sb.append(this.request);
        if (this.producer != null) {
            sb.append(' ');
            sb.append(this.producer);
        }
        return sb.toString();
    }
}

