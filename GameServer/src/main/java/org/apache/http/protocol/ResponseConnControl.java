package org.apache.http.protocol;

import org.apache.http.*;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

import java.io.IOException;

@Immutable
public class ResponseConnControl
        implements HttpResponseInterceptor {
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");

        HttpCoreContext corecontext = HttpCoreContext.adapt(context);

        int status = response.getStatusLine().getStatusCode();
        if (status == 400 || status == 408 || status == 411 || status == 413 || status == 414 || status == 503 || status == 501) {

            response.setHeader("Connection", "Close");
            return;
        }
        Header explicit = response.getFirstHeader("Connection");
        if (explicit != null && "Close".equalsIgnoreCase(explicit.getValue())) {
            return;
        }

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
            if (entity.getContentLength() < 0L && (!entity.isChunked() || ver.lessEquals((ProtocolVersion) HttpVersion.HTTP_1_0))) {

                response.setHeader("Connection", "Close");

                return;
            }
        }
        HttpRequest request = corecontext.getRequest();
        if (request != null) {
            Header header = request.getFirstHeader("Connection");
            if (header != null) {
                response.setHeader("Connection", header.getValue());
            } else if (request.getProtocolVersion().lessEquals((ProtocolVersion) HttpVersion.HTTP_1_0)) {
                response.setHeader("Connection", "Close");
            }
        }
    }
}

