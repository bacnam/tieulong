/*    */ package org.apache.http.impl.nio.client;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
/*    */ import org.apache.http.impl.nio.reactor.IOReactorConfig;
/*    */ import org.apache.http.nio.conn.NHttpClientConnectionManager;
/*    */ import org.apache.http.nio.reactor.ConnectingIOReactor;
/*    */ import org.apache.http.util.Args;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class HttpAsyncClients
/*    */ {
/*    */   public static HttpAsyncClientBuilder custom() {
/* 54 */     return HttpAsyncClientBuilder.create();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CloseableHttpAsyncClient createDefault() {
/* 62 */     return HttpAsyncClientBuilder.create().build();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CloseableHttpAsyncClient createSystem() {
/* 70 */     return HttpAsyncClientBuilder.create().useSystemProperties().build();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CloseableHttpAsyncClient createMinimal() {
/* 78 */     return new MinimalHttpAsyncClient((NHttpClientConnectionManager)new PoolingNHttpClientConnectionManager(IOReactorUtils.create(IOReactorConfig.DEFAULT)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CloseableHttpAsyncClient createMinimal(ConnectingIOReactor ioreactor) {
/* 87 */     Args.notNull(ioreactor, "I/O reactor");
/* 88 */     return new MinimalHttpAsyncClient((NHttpClientConnectionManager)new PoolingNHttpClientConnectionManager(ioreactor));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CloseableHttpAsyncClient createMinimal(NHttpClientConnectionManager connManager) {
/* 97 */     Args.notNull(connManager, "Connection manager");
/* 98 */     return new MinimalHttpAsyncClient(connManager);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/HttpAsyncClients.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */