/*    */ package org.apache.http.impl.nio;
/*    */ 
/*    */ import org.apache.http.nio.reactor.IOSession;
/*    */ import org.apache.http.protocol.HttpContext;
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
/*    */ class SessionHttpContext
/*    */   implements HttpContext
/*    */ {
/*    */   private final IOSession iosession;
/*    */   
/*    */   public SessionHttpContext(IOSession iosession) {
/* 39 */     this.iosession = iosession;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getAttribute(String id) {
/* 44 */     return this.iosession.getAttribute(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object removeAttribute(String id) {
/* 49 */     return this.iosession.removeAttribute(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setAttribute(String id, Object obj) {
/* 54 */     this.iosession.setAttribute(id, obj);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/SessionHttpContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */