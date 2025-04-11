package org.apache.http.protocol;

import org.apache.http.*;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

import java.io.IOException;
import java.net.InetAddress;

@Immutable
public class RequestTargetHost
        implements HttpRequestInterceptor {
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");

        HttpCoreContext corecontext = HttpCoreContext.adapt(context);

        ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT") && ver.lessEquals((ProtocolVersion) HttpVersion.HTTP_1_0)) {
            return;
        }

        if (!request.containsHeader("Host")) {
            HttpHost targethost = corecontext.getTargetHost();
            if (targethost == null) {
                HttpConnection conn = corecontext.getConnection();
                if (conn instanceof HttpInetConnection) {

                    InetAddress address = ((HttpInetConnection) conn).getRemoteAddress();
                    int port = ((HttpInetConnection) conn).getRemotePort();
                    if (address != null) {
                        targethost = new HttpHost(address.getHostName(), port);
                    }
                }
                if (targethost == null) {
                    if (ver.lessEquals((ProtocolVersion) HttpVersion.HTTP_1_0)) {
                        return;
                    }
                    throw new ProtocolException("Target host missing");
                }
            }

            request.addHeader("Host", targethost.toHostString());
        }
    }
}

