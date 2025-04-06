/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.nio.reactor.IOEventDispatch;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLIOSession;
/*     */ import org.apache.http.util.Asserts;
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
/*     */ @Immutable
/*     */ public abstract class AbstractIODispatch<T>
/*     */   implements IOEventDispatch
/*     */ {
/*     */   protected abstract T createConnection(IOSession paramIOSession);
/*     */   
/*     */   protected abstract void onConnected(T paramT);
/*     */   
/*     */   protected abstract void onClosed(T paramT);
/*     */   
/*     */   protected abstract void onException(T paramT, IOException paramIOException);
/*     */   
/*     */   protected abstract void onInputReady(T paramT);
/*     */   
/*     */   protected abstract void onOutputReady(T paramT);
/*     */   
/*     */   protected abstract void onTimeout(T paramT);
/*     */   
/*     */   private void ensureNotNull(T conn) {
/*  63 */     Asserts.notNull(conn, "HTTP connection");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void connected(IOSession session) {
/*  69 */     T conn = (T)session.getAttribute("http.connection");
/*     */     try {
/*  71 */       if (conn == null) {
/*  72 */         conn = createConnection(session);
/*  73 */         session.setAttribute("http.connection", conn);
/*     */       } 
/*  75 */       onConnected(conn);
/*  76 */       SSLIOSession ssliosession = (SSLIOSession)session.getAttribute("http.session.ssl");
/*     */       
/*  78 */       if (ssliosession != null) {
/*     */         try {
/*  80 */           synchronized (ssliosession) {
/*  81 */             if (!ssliosession.isInitialized()) {
/*  82 */               ssliosession.initialize();
/*     */             }
/*     */           } 
/*  85 */         } catch (IOException ex) {
/*  86 */           onException(conn, ex);
/*  87 */           ssliosession.shutdown();
/*     */         } 
/*     */       }
/*  90 */     } catch (RuntimeException ex) {
/*  91 */       session.shutdown();
/*  92 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disconnected(IOSession session) {
/* 100 */     T conn = (T)session.getAttribute("http.connection");
/* 101 */     if (conn != null) {
/* 102 */       onClosed(conn);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inputReady(IOSession session) {
/* 110 */     T conn = (T)session.getAttribute("http.connection");
/*     */     try {
/* 112 */       ensureNotNull(conn);
/* 113 */       SSLIOSession ssliosession = (SSLIOSession)session.getAttribute("http.session.ssl");
/*     */       
/* 115 */       if (ssliosession == null) {
/* 116 */         onInputReady(conn);
/*     */       } else {
/*     */         try {
/* 119 */           if (!ssliosession.isInitialized()) {
/* 120 */             ssliosession.initialize();
/*     */           }
/* 122 */           if (ssliosession.isAppInputReady()) {
/* 123 */             onInputReady(conn);
/*     */           }
/* 125 */           ssliosession.inboundTransport();
/* 126 */         } catch (IOException ex) {
/* 127 */           onException(conn, ex);
/* 128 */           ssliosession.shutdown();
/*     */         } 
/*     */       } 
/* 131 */     } catch (RuntimeException ex) {
/* 132 */       session.shutdown();
/* 133 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void outputReady(IOSession session) {
/* 141 */     T conn = (T)session.getAttribute("http.connection");
/*     */     try {
/* 143 */       ensureNotNull(conn);
/* 144 */       SSLIOSession ssliosession = (SSLIOSession)session.getAttribute("http.session.ssl");
/*     */       
/* 146 */       if (ssliosession == null) {
/* 147 */         onOutputReady(conn);
/*     */       } else {
/*     */         try {
/* 150 */           if (!ssliosession.isInitialized()) {
/* 151 */             ssliosession.initialize();
/*     */           }
/* 153 */           if (ssliosession.isAppOutputReady()) {
/* 154 */             onOutputReady(conn);
/*     */           }
/* 156 */           ssliosession.outboundTransport();
/* 157 */         } catch (IOException ex) {
/* 158 */           onException(conn, ex);
/* 159 */           ssliosession.shutdown();
/*     */         } 
/*     */       } 
/* 162 */     } catch (RuntimeException ex) {
/* 163 */       session.shutdown();
/* 164 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void timeout(IOSession session) {
/* 172 */     T conn = (T)session.getAttribute("http.connection");
/*     */     try {
/* 174 */       SSLIOSession ssliosession = (SSLIOSession)session.getAttribute("http.session.ssl");
/*     */       
/* 176 */       ensureNotNull(conn);
/* 177 */       onTimeout(conn);
/* 178 */       if (ssliosession != null) {
/* 179 */         synchronized (ssliosession) {
/* 180 */           if (ssliosession.isOutboundDone() && !ssliosession.isInboundDone())
/*     */           {
/* 182 */             ssliosession.shutdown();
/*     */           }
/*     */         } 
/*     */       }
/* 186 */     } catch (RuntimeException ex) {
/* 187 */       session.shutdown();
/* 188 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/AbstractIODispatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */