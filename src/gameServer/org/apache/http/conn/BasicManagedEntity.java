/*     */ package org.apache.http.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.SocketException;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.HttpEntityWrapper;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.EntityUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public class BasicManagedEntity
/*     */   extends HttpEntityWrapper
/*     */   implements ConnectionReleaseTrigger, EofSensorWatcher
/*     */ {
/*     */   protected ManagedClientConnection managedConn;
/*     */   protected final boolean attemptReuse;
/*     */   
/*     */   public BasicManagedEntity(HttpEntity entity, ManagedClientConnection conn, boolean reuse) {
/*  74 */     super(entity);
/*  75 */     Args.notNull(conn, "Connection");
/*  76 */     this.managedConn = conn;
/*  77 */     this.attemptReuse = reuse;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  87 */     return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
/*     */   }
/*     */   
/*     */   private void ensureConsumed() throws IOException {
/*  91 */     if (this.managedConn == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  96 */       if (this.attemptReuse) {
/*     */         
/*  98 */         EntityUtils.consume(this.wrappedEntity);
/*  99 */         this.managedConn.markReusable();
/*     */       } else {
/* 101 */         this.managedConn.unmarkReusable();
/*     */       } 
/*     */     } finally {
/* 104 */       releaseManagedConnection();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void consumeContent() throws IOException {
/* 114 */     ensureConsumed();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 119 */     super.writeTo(outstream);
/* 120 */     ensureConsumed();
/*     */   }
/*     */   
/*     */   public void releaseConnection() throws IOException {
/* 124 */     ensureConsumed();
/*     */   }
/*     */ 
/*     */   
/*     */   public void abortConnection() throws IOException {
/* 129 */     if (this.managedConn != null) {
/*     */       try {
/* 131 */         this.managedConn.abortConnection();
/*     */       } finally {
/* 133 */         this.managedConn = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean eofDetected(InputStream wrapped) throws IOException {
/*     */     try {
/* 140 */       if (this.managedConn != null) {
/* 141 */         if (this.attemptReuse) {
/*     */ 
/*     */           
/* 144 */           wrapped.close();
/* 145 */           this.managedConn.markReusable();
/*     */         } else {
/* 147 */           this.managedConn.unmarkReusable();
/*     */         } 
/*     */       }
/*     */     } finally {
/* 151 */       releaseManagedConnection();
/*     */     } 
/* 153 */     return false;
/*     */   }
/*     */   
/*     */   public boolean streamClosed(InputStream wrapped) throws IOException {
/*     */     try {
/* 158 */       if (this.managedConn != null) {
/* 159 */         if (this.attemptReuse) {
/* 160 */           boolean valid = this.managedConn.isOpen();
/*     */ 
/*     */           
/*     */           try {
/* 164 */             wrapped.close();
/* 165 */             this.managedConn.markReusable();
/* 166 */           } catch (SocketException ex) {
/* 167 */             if (valid) {
/* 168 */               throw ex;
/*     */             }
/*     */           } 
/*     */         } else {
/* 172 */           this.managedConn.unmarkReusable();
/*     */         } 
/*     */       }
/*     */     } finally {
/* 176 */       releaseManagedConnection();
/*     */     } 
/* 178 */     return false;
/*     */   }
/*     */   
/*     */   public boolean streamAbort(InputStream wrapped) throws IOException {
/* 182 */     if (this.managedConn != null) {
/* 183 */       this.managedConn.abortConnection();
/*     */     }
/* 185 */     return false;
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
/*     */   protected void releaseManagedConnection() throws IOException {
/* 199 */     if (this.managedConn != null)
/*     */       try {
/* 201 */         this.managedConn.releaseConnection();
/*     */       } finally {
/* 203 */         this.managedConn = null;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/conn/BasicManagedEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */