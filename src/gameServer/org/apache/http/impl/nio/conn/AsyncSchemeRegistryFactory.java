/*    */ package org.apache.http.impl.nio.conn;
/*    */ 
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.nio.conn.scheme.AsyncScheme;
/*    */ import org.apache.http.nio.conn.scheme.AsyncSchemeRegistry;
/*    */ import org.apache.http.nio.conn.scheme.LayeringStrategy;
/*    */ import org.apache.http.nio.conn.ssl.SSLLayeringStrategy;
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
/*    */ @Deprecated
/*    */ @ThreadSafe
/*    */ public final class AsyncSchemeRegistryFactory
/*    */ {
/*    */   public static AsyncSchemeRegistry createDefault() {
/* 39 */     AsyncSchemeRegistry registry = new AsyncSchemeRegistry();
/* 40 */     registry.register(new AsyncScheme("http", 80, null));
/*    */     
/* 42 */     registry.register(new AsyncScheme("https", 443, (LayeringStrategy)SSLLayeringStrategy.getDefaultStrategy()));
/*    */     
/* 44 */     return registry;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/AsyncSchemeRegistryFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */