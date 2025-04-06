/*    */ package org.apache.mina.filter.ssl;
/*    */ 
/*    */ import java.security.InvalidAlgorithmParameterException;
/*    */ import java.security.KeyStore;
/*    */ import java.security.KeyStoreException;
/*    */ import java.security.Provider;
/*    */ import java.security.cert.CertificateException;
/*    */ import java.security.cert.X509Certificate;
/*    */ import javax.net.ssl.ManagerFactoryParameters;
/*    */ import javax.net.ssl.TrustManager;
/*    */ import javax.net.ssl.TrustManagerFactory;
/*    */ import javax.net.ssl.TrustManagerFactorySpi;
/*    */ import javax.net.ssl.X509TrustManager;
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
/*    */ public class BogusTrustManagerFactory
/*    */   extends TrustManagerFactory
/*    */ {
/*    */   public BogusTrustManagerFactory() {
/* 44 */     super(new BogusTrustManagerFactorySpi(null), new Provider("MinaBogus", 1.0D, "") { private static final long serialVersionUID = -4024169055312053827L; }"MinaBogus");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   private static final X509TrustManager X509 = new X509TrustManager()
/*    */     {
/*    */       public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
/*    */ 
/*    */ 
/*    */       
/*    */       public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}
/*    */ 
/*    */       
/*    */       public X509Certificate[] getAcceptedIssuers() {
/* 59 */         return new X509Certificate[0];
/*    */       }
/*    */     };
/*    */   
/* 63 */   private static final TrustManager[] X509_MANAGERS = new TrustManager[] { X509 };
/*    */   
/*    */   private static class BogusTrustManagerFactorySpi extends TrustManagerFactorySpi {
/*    */     private BogusTrustManagerFactorySpi() {}
/*    */     
/*    */     protected TrustManager[] engineGetTrustManagers() {
/* 69 */       return BogusTrustManagerFactory.X509_MANAGERS;
/*    */     }
/*    */     
/*    */     protected void engineInit(KeyStore keystore) throws KeyStoreException {}
/*    */     
/*    */     protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {}
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/ssl/BogusTrustManagerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */