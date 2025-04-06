/*    */ package org.apache.http.impl.nio.pool;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.nio.NHttpClientConnection;
/*    */ import org.apache.http.pool.PoolEntry;
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
/*    */ @ThreadSafe
/*    */ public class BasicNIOPoolEntry
/*    */   extends PoolEntry<HttpHost, NHttpClientConnection>
/*    */ {
/*    */   private volatile int socketTimeout;
/*    */   
/*    */   public BasicNIOPoolEntry(String id, HttpHost route, NHttpClientConnection conn) {
/* 50 */     super(id, route, conn);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 56 */       ((NHttpClientConnection)getConnection()).close();
/* 57 */     } catch (IOException ignore) {}
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 63 */     return !((NHttpClientConnection)getConnection()).isOpen();
/*    */   }
/*    */   
/*    */   int getSocketTimeout() {
/* 67 */     return this.socketTimeout;
/*    */   }
/*    */   
/*    */   void setSocketTimeout(int socketTimeout) {
/* 71 */     this.socketTimeout = socketTimeout;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/pool/BasicNIOPoolEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */