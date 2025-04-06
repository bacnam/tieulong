/*    */ package org.apache.http.impl.nio.codecs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.message.LineFormatter;
/*    */ import org.apache.http.nio.reactor.SessionOutputBuffer;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.util.CharArrayBuffer;
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
/*    */ @NotThreadSafe
/*    */ public class DefaultHttpRequestWriter
/*    */   extends AbstractMessageWriter<HttpRequest>
/*    */ {
/*    */   @Deprecated
/*    */   public DefaultHttpRequestWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
/* 57 */     super(buffer, formatter, params);
/*    */   }
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
/*    */   public DefaultHttpRequestWriter(SessionOutputBuffer buffer, LineFormatter formatter) {
/* 72 */     super(buffer, formatter);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultHttpRequestWriter(SessionOutputBuffer buffer) {
/* 79 */     super(buffer, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeHeadLine(HttpRequest message) throws IOException {
/* 84 */     CharArrayBuffer buffer = this.lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
/*    */     
/* 86 */     this.sessionBuffer.writeLine(buffer);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/codecs/DefaultHttpRequestWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */