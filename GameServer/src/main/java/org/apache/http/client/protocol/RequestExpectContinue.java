package org.apache.http.client.protocol;

import org.apache.http.*;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

import java.io.IOException;

@Immutable
public class RequestExpectContinue
        implements HttpRequestInterceptor {
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");

        if (!request.containsHeader("Expect") &&
                request instanceof HttpEntityEnclosingRequest) {
            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();

            if (entity != null && entity.getContentLength() != 0L && !ver.lessEquals((ProtocolVersion) HttpVersion.HTTP_1_0)) {

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                RequestConfig config = clientContext.getRequestConfig();
                if (config.isExpectContinueEnabled())
                    request.addHeader("Expect", "100-continue");
            }
        }
    }
}

