package org.apache.http.protocol;

import org.apache.http.*;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

import java.io.IOException;

@Immutable
public class RequestExpectContinue
        implements HttpRequestInterceptor {
    private final boolean activeByDefault;

    @Deprecated
    public RequestExpectContinue() {
        this(false);
    }

    public RequestExpectContinue(boolean activeByDefault) {
        this.activeByDefault = activeByDefault;
    }

    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");

        if (!request.containsHeader("Expect") &&
                request instanceof HttpEntityEnclosingRequest) {
            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();

            if (entity != null && entity.getContentLength() != 0L && !ver.lessEquals((ProtocolVersion) HttpVersion.HTTP_1_0)) {

                boolean active = request.getParams().getBooleanParameter("http.protocol.expect-continue", this.activeByDefault);

                if (active)
                    request.addHeader("Expect", "100-continue");
            }
        }
    }
}

