/*    */ package org.apache.http.nio.conn.ssl;
/*    */ 
/*    */ import java.security.cert.CertificateException;
/*    */ import java.security.cert.X509Certificate;
/*    */ import javax.net.ssl.X509TrustManager;
/*    */ import org.apache.http.conn.ssl.TrustStrategy;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ class TrustManagerDecorator
/*    */   implements X509TrustManager
/*    */ {
/*    */   private final X509TrustManager trustManager;
/*    */   private final TrustStrategy trustStrategy;
/*    */   
/*    */   TrustManagerDecorator(X509TrustManager trustManager, TrustStrategy trustStrategy) {
/* 44 */     this.trustManager = trustManager;
/* 45 */     this.trustStrategy = trustStrategy;
/*    */   }
/*    */ 
/*    */   
/*    */   public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/* 50 */     this.trustManager.checkClientTrusted(chain, authType);
/*    */   }
/*    */ 
/*    */   
/*    */   public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/* 55 */     if (!this.trustStrategy.isTrusted(chain, authType)) {
/* 56 */       this.trustManager.checkServerTrusted(chain, authType);
/*    */     }
/*    */   }
/*    */   
/*    */   public X509Certificate[] getAcceptedIssuers() {
/* 61 */     return this.trustManager.getAcceptedIssuers();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/ssl/TrustManagerDecorator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */