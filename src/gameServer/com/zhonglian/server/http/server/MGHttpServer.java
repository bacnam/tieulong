package com.zhonglian.server.http.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import java.io.IOException;
import java.net.InetSocketAddress;

public class MGHttpServer
{
public static HttpServer createServer(int port, HttpDispather handler, String path) throws IOException {
HttpServer httpserver = null;
HttpServerProvider provider = HttpServerProvider.provider();
httpserver = provider.createHttpServer(new InetSocketAddress(port), 100);

httpserver.createContext(path, handler);
return httpserver;
}

public static HttpServer createServer(int port, HttpDispather handler) throws IOException {
return createServer(port, handler, "/");
}
}

