/*     */ package org.apache.mina.filter.ssl;
/*     */ 
/*     */ import java.security.KeyStore;
/*     */ import java.security.SecureRandom;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.ManagerFactoryParameters;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SslContextFactory
/*     */ {
/*  56 */   private String provider = null;
/*     */   
/*  58 */   private String protocol = "TLS";
/*     */   
/*  60 */   private SecureRandom secureRandom = null;
/*     */   
/*  62 */   private KeyStore keyManagerFactoryKeyStore = null;
/*     */   
/*  64 */   private char[] keyManagerFactoryKeyStorePassword = null;
/*     */   
/*  66 */   private KeyManagerFactory keyManagerFactory = null;
/*     */   
/*  68 */   private String keyManagerFactoryAlgorithm = null;
/*     */   
/*  70 */   private String keyManagerFactoryProvider = null;
/*     */   
/*     */   private boolean keyManagerFactoryAlgorithmUseDefault = true;
/*     */   
/*  74 */   private KeyStore trustManagerFactoryKeyStore = null;
/*     */   
/*  76 */   private TrustManagerFactory trustManagerFactory = null;
/*     */   
/*  78 */   private String trustManagerFactoryAlgorithm = null;
/*     */   
/*  80 */   private String trustManagerFactoryProvider = null;
/*     */   
/*     */   private boolean trustManagerFactoryAlgorithmUseDefault = true;
/*     */   
/*  84 */   private ManagerFactoryParameters trustManagerFactoryParameters = null;
/*     */   
/*  86 */   private int clientSessionCacheSize = -1;
/*     */   
/*  88 */   private int clientSessionTimeout = -1;
/*     */   
/*  90 */   private int serverSessionCacheSize = -1;
/*     */   
/*  92 */   private int serverSessionTimeout = -1;
/*     */   
/*     */   public SSLContext newInstance() throws Exception {
/*  95 */     KeyManagerFactory kmf = this.keyManagerFactory;
/*  96 */     TrustManagerFactory tmf = this.trustManagerFactory;
/*     */     
/*  98 */     if (kmf == null) {
/*  99 */       String algorithm = this.keyManagerFactoryAlgorithm;
/* 100 */       if (algorithm == null && this.keyManagerFactoryAlgorithmUseDefault) {
/* 101 */         algorithm = KeyManagerFactory.getDefaultAlgorithm();
/*     */       }
/* 103 */       if (algorithm != null) {
/* 104 */         if (this.keyManagerFactoryProvider == null) {
/* 105 */           kmf = KeyManagerFactory.getInstance(algorithm);
/*     */         } else {
/* 107 */           kmf = KeyManagerFactory.getInstance(algorithm, this.keyManagerFactoryProvider);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 112 */     if (tmf == null) {
/* 113 */       String algorithm = this.trustManagerFactoryAlgorithm;
/* 114 */       if (algorithm == null && this.trustManagerFactoryAlgorithmUseDefault) {
/* 115 */         algorithm = TrustManagerFactory.getDefaultAlgorithm();
/*     */       }
/* 117 */       if (algorithm != null) {
/* 118 */         if (this.trustManagerFactoryProvider == null) {
/* 119 */           tmf = TrustManagerFactory.getInstance(algorithm);
/*     */         } else {
/* 121 */           tmf = TrustManagerFactory.getInstance(algorithm, this.trustManagerFactoryProvider);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 126 */     KeyManager[] keyManagers = null;
/* 127 */     if (kmf != null) {
/* 128 */       kmf.init(this.keyManagerFactoryKeyStore, this.keyManagerFactoryKeyStorePassword);
/* 129 */       keyManagers = kmf.getKeyManagers();
/*     */     } 
/* 131 */     TrustManager[] trustManagers = null;
/* 132 */     if (tmf != null) {
/* 133 */       if (this.trustManagerFactoryParameters != null) {
/* 134 */         tmf.init(this.trustManagerFactoryParameters);
/*     */       } else {
/* 136 */         tmf.init(this.trustManagerFactoryKeyStore);
/*     */       } 
/* 138 */       trustManagers = tmf.getTrustManagers();
/*     */     } 
/*     */     
/* 141 */     SSLContext context = null;
/* 142 */     if (this.provider == null) {
/* 143 */       context = SSLContext.getInstance(this.protocol);
/*     */     } else {
/* 145 */       context = SSLContext.getInstance(this.protocol, this.provider);
/*     */     } 
/*     */     
/* 148 */     context.init(keyManagers, trustManagers, this.secureRandom);
/*     */     
/* 150 */     if (this.clientSessionCacheSize >= 0) {
/* 151 */       context.getClientSessionContext().setSessionCacheSize(this.clientSessionCacheSize);
/*     */     }
/*     */     
/* 154 */     if (this.clientSessionTimeout >= 0) {
/* 155 */       context.getClientSessionContext().setSessionTimeout(this.clientSessionTimeout);
/*     */     }
/*     */     
/* 158 */     if (this.serverSessionCacheSize >= 0) {
/* 159 */       context.getServerSessionContext().setSessionCacheSize(this.serverSessionCacheSize);
/*     */     }
/*     */     
/* 162 */     if (this.serverSessionTimeout >= 0) {
/* 163 */       context.getServerSessionContext().setSessionTimeout(this.serverSessionTimeout);
/*     */     }
/*     */     
/* 166 */     return context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProvider(String provider) {
/* 176 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProtocol(String protocol) {
/* 186 */     if (protocol == null) {
/* 187 */       throw new IllegalArgumentException("protocol");
/*     */     }
/* 189 */     this.protocol = protocol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyManagerFactoryAlgorithmUseDefault(boolean useDefault) {
/* 204 */     this.keyManagerFactoryAlgorithmUseDefault = useDefault;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrustManagerFactoryAlgorithmUseDefault(boolean useDefault) {
/* 218 */     this.trustManagerFactoryAlgorithmUseDefault = useDefault;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyManagerFactory(KeyManagerFactory factory) {
/* 229 */     this.keyManagerFactory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyManagerFactoryAlgorithm(String algorithm) {
/* 251 */     this.keyManagerFactoryAlgorithm = algorithm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyManagerFactoryProvider(String provider) {
/* 272 */     this.keyManagerFactoryProvider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyManagerFactoryKeyStore(KeyStore keyStore) {
/* 283 */     this.keyManagerFactoryKeyStore = keyStore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyManagerFactoryKeyStorePassword(String password) {
/* 294 */     if (password != null) {
/* 295 */       this.keyManagerFactoryKeyStorePassword = password.toCharArray();
/*     */     } else {
/* 297 */       this.keyManagerFactoryKeyStorePassword = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrustManagerFactory(TrustManagerFactory factory) {
/* 310 */     this.trustManagerFactory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrustManagerFactoryAlgorithm(String algorithm) {
/* 332 */     this.trustManagerFactoryAlgorithm = algorithm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrustManagerFactoryKeyStore(KeyStore keyStore) {
/* 347 */     this.trustManagerFactoryKeyStore = keyStore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrustManagerFactoryParameters(ManagerFactoryParameters parameters) {
/* 358 */     this.trustManagerFactoryParameters = parameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrustManagerFactoryProvider(String provider) {
/* 379 */     this.trustManagerFactoryProvider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSecureRandom(SecureRandom secureRandom) {
/* 391 */     this.secureRandom = secureRandom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientSessionCacheSize(int size) {
/* 401 */     this.clientSessionCacheSize = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientSessionTimeout(int seconds) {
/* 411 */     this.clientSessionTimeout = seconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServerSessionCacheSize(int serverSessionCacheSize) {
/* 421 */     this.serverSessionCacheSize = serverSessionCacheSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServerSessionTimeout(int serverSessionTimeout) {
/* 431 */     this.serverSessionTimeout = serverSessionTimeout;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/ssl/SslContextFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */