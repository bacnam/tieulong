/*    */ package org.apache.http.impl.nio.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.impl.nio.DefaultNHttpClientConnection;
/*    */ import org.apache.http.impl.nio.reactor.AbstractIODispatch;
/*    */ import org.apache.http.nio.NHttpClientConnection;
/*    */ import org.apache.http.nio.NHttpClientEventHandler;
/*    */ import org.apache.http.nio.reactor.IOSession;
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
/*    */ class InternalIODispatch
/*    */   extends AbstractIODispatch<DefaultNHttpClientConnection>
/*    */ {
/* 43 */   private final NHttpClientEventHandler handler = (NHttpClientEventHandler)new LoggingAsyncRequestExecutor();
/*    */ 
/*    */ 
/*    */   
/*    */   protected DefaultNHttpClientConnection createConnection(IOSession session) {
/* 48 */     throw new IllegalStateException("Connection must be created by connection manager");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onConnected(DefaultNHttpClientConnection conn) {
/* 53 */     Object attachment = conn.getContext().getAttribute("http.session.attachment");
/*    */     try {
/* 55 */       this.handler.connected((NHttpClientConnection)conn, attachment);
/* 56 */     } catch (Exception ex) {
/* 57 */       this.handler.exception((NHttpClientConnection)conn, ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onClosed(DefaultNHttpClientConnection conn) {
/* 63 */     this.handler.closed((NHttpClientConnection)conn);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onException(DefaultNHttpClientConnection conn, IOException ex) {
/* 68 */     this.handler.exception((NHttpClientConnection)conn, ex);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onInputReady(DefaultNHttpClientConnection conn) {
/* 73 */     conn.consumeInput(this.handler);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onOutputReady(DefaultNHttpClientConnection conn) {
/* 78 */     conn.produceOutput(this.handler);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onTimeout(DefaultNHttpClientConnection conn) {
/*    */     try {
/* 84 */       this.handler.timeout((NHttpClientConnection)conn);
/* 85 */     } catch (Exception ex) {
/* 86 */       this.handler.exception((NHttpClientConnection)conn, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/InternalIODispatch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */