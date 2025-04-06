/*    */ package org.apache.http.nio.client.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
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
/*    */ public class HttpAsyncClientUtils
/*    */ {
/*    */   public static void closeQuietly(CloseableHttpAsyncClient httpAsyncClient) {
/* 62 */     if (httpAsyncClient != null)
/*    */       try {
/* 64 */         httpAsyncClient.close();
/* 65 */       } catch (IOException ignore) {} 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/client/util/HttpAsyncClientUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */