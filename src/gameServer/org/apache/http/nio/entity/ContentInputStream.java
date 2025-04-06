/*    */ package org.apache.http.nio.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.io.BufferInfo;
/*    */ import org.apache.http.nio.util.ContentInputBuffer;
/*    */ import org.apache.http.util.Args;
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
/*    */ public class ContentInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private final ContentInputBuffer buffer;
/*    */   
/*    */   public ContentInputStream(ContentInputBuffer buffer) {
/* 50 */     Args.notNull(buffer, "Input buffer");
/* 51 */     this.buffer = buffer;
/*    */   }
/*    */ 
/*    */   
/*    */   public int available() throws IOException {
/* 56 */     if (this.buffer instanceof BufferInfo) {
/* 57 */       return ((BufferInfo)this.buffer).length();
/*    */     }
/* 59 */     return super.available();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 65 */     return this.buffer.read(b, off, len);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b) throws IOException {
/* 70 */     if (b == null) {
/* 71 */       return 0;
/*    */     }
/* 73 */     return this.buffer.read(b, 0, b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 78 */     return this.buffer.read();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 84 */     byte[] tmp = new byte[1024];
/* 85 */     while (this.buffer.read(tmp, 0, tmp.length) >= 0);
/*    */     
/* 87 */     super.close();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/entity/ContentInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */