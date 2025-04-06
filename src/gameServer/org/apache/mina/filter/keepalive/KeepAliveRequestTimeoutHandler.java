/*    */ package org.apache.mina.filter.keepalive;
/*    */ 
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public interface KeepAliveRequestTimeoutHandler
/*    */ {
/* 36 */   public static final KeepAliveRequestTimeoutHandler NOOP = new KeepAliveRequestTimeoutHandler()
/*    */     {
/*    */       public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {}
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static final KeepAliveRequestTimeoutHandler LOG = new KeepAliveRequestTimeoutHandler() {
/* 46 */       private final Logger LOGGER = LoggerFactory.getLogger(KeepAliveFilter.class);
/*    */       
/*    */       public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
/* 49 */         this.LOGGER.warn("A keep-alive response message was not received within {} second(s).", Integer.valueOf(filter.getRequestTimeout()));
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public static final KeepAliveRequestTimeoutHandler EXCEPTION = new KeepAliveRequestTimeoutHandler() {
/*    */       public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
/* 59 */         throw new KeepAliveRequestTimeoutException("A keep-alive response message was not received within " + filter.getRequestTimeout() + " second(s).");
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public static final KeepAliveRequestTimeoutHandler CLOSE = new KeepAliveRequestTimeoutHandler() {
/* 68 */       private final Logger LOGGER = LoggerFactory.getLogger(KeepAliveFilter.class);
/*    */       
/*    */       public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
/* 71 */         this.LOGGER.warn("Closing the session because a keep-alive response message was not received within {} second(s).", Integer.valueOf(filter.getRequestTimeout()));
/*    */         
/* 73 */         session.close(true);
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 80 */   public static final KeepAliveRequestTimeoutHandler DEAF_SPEAKER = new KeepAliveRequestTimeoutHandler() {
/*    */       public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
/* 82 */         throw new Error("Shouldn't be invoked.  Please file a bug report.");
/*    */       }
/*    */     };
/*    */   
/*    */   void keepAliveRequestTimedOut(KeepAliveFilter paramKeepAliveFilter, IoSession paramIoSession) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/keepalive/KeepAliveRequestTimeoutHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */