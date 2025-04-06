/*    */ package org.apache.http.impl.nio.client;
/*    */ 
/*    */ import java.util.Queue;
/*    */ import org.apache.http.concurrent.BasicFuture;
/*    */ import org.apache.http.nio.protocol.HttpAsyncRequestExecutionHandler;
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
/*    */ @Deprecated
/*    */ class DefaultResultCallback<T>
/*    */   implements ResultCallback<T>
/*    */ {
/*    */   private final BasicFuture<T> future;
/*    */   private final Queue<HttpAsyncRequestExecutionHandler<?>> queue;
/*    */   
/*    */   DefaultResultCallback(BasicFuture<T> future, Queue<HttpAsyncRequestExecutionHandler<?>> queue) {
/* 43 */     this.future = future;
/* 44 */     this.queue = queue;
/*    */   }
/*    */   
/*    */   public void completed(T result, HttpAsyncRequestExecutionHandler<T> handler) {
/* 48 */     this.future.completed(result);
/* 49 */     this.queue.remove(handler);
/*    */   }
/*    */   
/*    */   public void failed(Exception ex, HttpAsyncRequestExecutionHandler<T> handler) {
/* 53 */     this.future.failed(ex);
/* 54 */     this.queue.remove(handler);
/*    */   }
/*    */   
/*    */   public void cancelled(HttpAsyncRequestExecutionHandler<T> handler) {
/* 58 */     this.future.cancel(true);
/* 59 */     this.queue.remove(handler);
/*    */   }
/*    */   
/*    */   public boolean isDone() {
/* 63 */     return this.future.isDone();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/DefaultResultCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */