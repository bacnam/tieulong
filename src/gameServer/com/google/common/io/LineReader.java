/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.nio.CharBuffer;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
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
/*    */ @Beta
/*    */ public final class LineReader
/*    */ {
/*    */   private final Readable readable;
/*    */   private final Reader reader;
/* 40 */   private final char[] buf = new char[4096];
/* 41 */   private final CharBuffer cbuf = CharBuffer.wrap(this.buf);
/*    */   
/* 43 */   private final Queue<String> lines = new LinkedList<String>();
/* 44 */   private final LineBuffer lineBuf = new LineBuffer() {
/*    */       protected void handleLine(String line, String end) {
/* 46 */         LineReader.this.lines.add(line);
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LineReader(Readable readable) {
/* 55 */     Preconditions.checkNotNull(readable);
/* 56 */     this.readable = readable;
/* 57 */     this.reader = (readable instanceof Reader) ? (Reader)readable : null;
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
/*    */   public String readLine() throws IOException {
/* 72 */     while (this.lines.peek() == null) {
/* 73 */       this.cbuf.clear();
/*    */ 
/*    */       
/* 76 */       int read = (this.reader != null) ? this.reader.read(this.buf, 0, this.buf.length) : this.readable.read(this.cbuf);
/*    */ 
/*    */       
/* 79 */       if (read == -1) {
/* 80 */         this.lineBuf.finish();
/*    */         break;
/*    */       } 
/* 83 */       this.lineBuf.add(this.buf, 0, read);
/*    */     } 
/* 85 */     return this.lines.poll();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/io/LineReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */