/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ 
/*    */ 
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
/*    */ public class RequestAcceptEncoding
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   private final String acceptEncoding;
/*    */   
/*    */   public RequestAcceptEncoding(List<String> encodings) {
/* 56 */     if (encodings != null && !encodings.isEmpty()) {
/* 57 */       StringBuilder buf = new StringBuilder();
/* 58 */       for (int i = 0; i < encodings.size(); i++) {
/* 59 */         if (i > 0) {
/* 60 */           buf.append(",");
/*    */         }
/* 62 */         buf.append(encodings.get(i));
/*    */       } 
/* 64 */       this.acceptEncoding = buf.toString();
/*    */     } else {
/* 66 */       this.acceptEncoding = "gzip,deflate";
/*    */     } 
/*    */   }
/*    */   
/*    */   public RequestAcceptEncoding() {
/* 71 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 80 */     if (!request.containsHeader("Accept-Encoding"))
/* 81 */       request.addHeader("Accept-Encoding", this.acceptEncoding); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/client/protocol/RequestAcceptEncoding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */