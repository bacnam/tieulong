/*    */ package com.zhonglian.server.http.server;
/*    */ 
/*    */ import com.sun.net.httpserver.HttpServer;
/*    */ import com.sun.net.httpserver.spi.HttpServerProvider;
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ 
/*    */ public class MGHttpServer
/*    */ {
/*    */   public static HttpServer createServer(int port, HttpDispather handler, String path) throws IOException {
/* 11 */     HttpServer httpserver = null;
/* 12 */     HttpServerProvider provider = HttpServerProvider.provider();
/* 13 */     httpserver = provider.createHttpServer(new InetSocketAddress(port), 100);
/*    */     
/* 15 */     httpserver.createContext(path, handler);
/* 16 */     return httpserver;
/*    */   }
/*    */   
/*    */   public static HttpServer createServer(int port, HttpDispather handler) throws IOException {
/* 20 */     return createServer(port, handler, "/");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/server/MGHttpServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */