/*    */ package org.apache.mina.filter.stream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.mina.core.buffer.IoBuffer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StreamWriteFilter
/*    */   extends AbstractStreamWriteFilter<InputStream>
/*    */ {
/*    */   protected IoBuffer getNextBuffer(InputStream is) throws IOException {
/* 58 */     byte[] bytes = new byte[getWriteBufferSize()];
/*    */     
/* 60 */     int off = 0;
/* 61 */     int n = 0;
/* 62 */     while (off < bytes.length && (n = is.read(bytes, off, bytes.length - off)) != -1) {
/* 63 */       off += n;
/*    */     }
/*    */     
/* 66 */     if (n == -1 && off == 0) {
/* 67 */       return null;
/*    */     }
/*    */     
/* 70 */     IoBuffer buffer = IoBuffer.wrap(bytes, 0, off);
/*    */     
/* 72 */     return buffer;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Class<InputStream> getMessageClass() {
/* 77 */     return InputStream.class;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/stream/StreamWriteFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */