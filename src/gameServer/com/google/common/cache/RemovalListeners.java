/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import java.util.concurrent.Executor;
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
/*    */ @Beta
/*    */ public final class RemovalListeners
/*    */ {
/*    */   public static <K, V> RemovalListener<K, V> asynchronous(final RemovalListener<K, V> listener, final Executor executor) {
/* 44 */     return new RemovalListener<K, V>()
/*    */       {
/*    */         public void onRemoval(final RemovalNotification<K, V> notification) {
/* 47 */           executor.execute(new Runnable()
/*    */               {
/*    */                 public void run() {
/* 50 */                   listener.onRemoval(notification);
/*    */                 }
/*    */               });
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/cache/RemovalListeners.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */