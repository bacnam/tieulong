/*     */ package org.apache.http.client.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Inflater;
/*     */ import java.util.zip.InflaterInputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeflateInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private InputStream sourceStream;
/*     */   
/*     */   public DeflateInputStream(InputStream wrapped) throws IOException {
/*  72 */     byte[] peeked = new byte[6];
/*     */     
/*  74 */     PushbackInputStream pushback = new PushbackInputStream(wrapped, peeked.length);
/*     */     
/*  76 */     int headerLength = pushback.read(peeked);
/*     */     
/*  78 */     if (headerLength == -1) {
/*  79 */       throw new IOException("Unable to read the response");
/*     */     }
/*     */ 
/*     */     
/*  83 */     byte[] dummy = new byte[1];
/*     */     
/*  85 */     Inflater inf = new Inflater();
/*     */     
/*     */     try {
/*     */       int n;
/*  89 */       while ((n = inf.inflate(dummy)) == 0) {
/*  90 */         if (inf.finished())
/*     */         {
/*     */           
/*  93 */           throw new IOException("Unable to read the response");
/*     */         }
/*     */         
/*  96 */         if (inf.needsDictionary()) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 102 */         if (inf.needsInput()) {
/* 103 */           inf.setInput(peeked);
/*     */         }
/*     */       } 
/*     */       
/* 107 */       if (n == -1) {
/* 108 */         throw new IOException("Unable to read the response");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 115 */       pushback.unread(peeked, 0, headerLength);
/* 116 */       this.sourceStream = new DeflateStream(pushback, new Inflater());
/* 117 */     } catch (DataFormatException e) {
/*     */ 
/*     */ 
/*     */       
/* 121 */       pushback.unread(peeked, 0, headerLength);
/* 122 */       this.sourceStream = new DeflateStream(pushback, new Inflater(true));
/*     */     } finally {
/* 124 */       inf.end();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 135 */     return this.sourceStream.read();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 144 */     return this.sourceStream.read(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 153 */     return this.sourceStream.read(b, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 162 */     return this.sourceStream.skip(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 171 */     return this.sourceStream.available();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readLimit) {
/* 179 */     this.sourceStream.mark(readLimit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/* 188 */     this.sourceStream.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 196 */     return this.sourceStream.markSupported();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 205 */     this.sourceStream.close();
/*     */   }
/*     */   
/*     */   static class DeflateStream
/*     */     extends InflaterInputStream {
/*     */     private boolean closed = false;
/*     */     
/*     */     public DeflateStream(InputStream in, Inflater inflater) {
/* 213 */       super(in, inflater);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 218 */       if (this.closed) {
/*     */         return;
/*     */       }
/* 221 */       this.closed = true;
/* 222 */       this.inf.end();
/* 223 */       super.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/client/entity/DeflateInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */