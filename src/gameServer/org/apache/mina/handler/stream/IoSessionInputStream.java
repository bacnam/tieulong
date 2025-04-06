/*     */ package org.apache.mina.handler.stream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
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
/*     */ class IoSessionInputStream
/*     */   extends InputStream
/*     */ {
/*  35 */   private final Object mutex = new Object();
/*     */   
/*     */   private final IoBuffer buf;
/*     */   
/*     */   private volatile boolean closed;
/*     */   
/*     */   private volatile boolean released;
/*     */   
/*     */   private IOException exception;
/*     */   
/*     */   public IoSessionInputStream() {
/*  46 */     this.buf = IoBuffer.allocate(16);
/*  47 */     this.buf.setAutoExpand(true);
/*  48 */     this.buf.limit(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/*  53 */     if (this.released) {
/*  54 */       return 0;
/*     */     }
/*     */     
/*  57 */     synchronized (this.mutex) {
/*  58 */       return this.buf.remaining();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  64 */     if (this.closed) {
/*     */       return;
/*     */     }
/*     */     
/*  68 */     synchronized (this.mutex) {
/*  69 */       this.closed = true;
/*  70 */       releaseBuffer();
/*     */       
/*  72 */       this.mutex.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  78 */     synchronized (this.mutex) {
/*  79 */       if (!waitForData()) {
/*  80 */         return -1;
/*     */       }
/*     */       
/*  83 */       return this.buf.get() & 0xFF;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  89 */     synchronized (this.mutex) {
/*  90 */       int readBytes; if (!waitForData()) {
/*  91 */         return -1;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  96 */       if (len > this.buf.remaining()) {
/*  97 */         readBytes = this.buf.remaining();
/*     */       } else {
/*  99 */         readBytes = len;
/*     */       } 
/*     */       
/* 102 */       this.buf.get(b, off, readBytes);
/*     */       
/* 104 */       return readBytes;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean waitForData() throws IOException {
/* 109 */     if (this.released) {
/* 110 */       return false;
/*     */     }
/*     */     
/* 113 */     synchronized (this.mutex) {
/* 114 */       while (!this.released && this.buf.remaining() == 0 && this.exception == null) {
/*     */         try {
/* 116 */           this.mutex.wait();
/* 117 */         } catch (InterruptedException e) {
/* 118 */           IOException ioe = new IOException("Interrupted while waiting for more data");
/* 119 */           ioe.initCause(e);
/* 120 */           throw ioe;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     if (this.exception != null) {
/* 126 */       releaseBuffer();
/* 127 */       throw this.exception;
/*     */     } 
/*     */     
/* 130 */     if (this.closed && this.buf.remaining() == 0) {
/* 131 */       releaseBuffer();
/*     */       
/* 133 */       return false;
/*     */     } 
/*     */     
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   private void releaseBuffer() {
/* 140 */     if (this.released) {
/*     */       return;
/*     */     }
/*     */     
/* 144 */     this.released = true;
/*     */   }
/*     */   
/*     */   public void write(IoBuffer src) {
/* 148 */     synchronized (this.mutex) {
/* 149 */       if (this.closed) {
/*     */         return;
/*     */       }
/*     */       
/* 153 */       if (this.buf.hasRemaining()) {
/* 154 */         this.buf.compact();
/* 155 */         this.buf.put(src);
/* 156 */         this.buf.flip();
/*     */       } else {
/* 158 */         this.buf.clear();
/* 159 */         this.buf.put(src);
/* 160 */         this.buf.flip();
/* 161 */         this.mutex.notifyAll();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void throwException(IOException e) {
/* 167 */     synchronized (this.mutex) {
/* 168 */       if (this.exception == null) {
/* 169 */         this.exception = e;
/*     */         
/* 171 */         this.mutex.notifyAll();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/handler/stream/IoSessionInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */