/*    */ package org.apache.mina.filter.stream;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.core.file.FileRegion;
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
/*    */ 
/*    */ public class FileRegionWriteFilter
/*    */   extends AbstractStreamWriteFilter<FileRegion>
/*    */ {
/*    */   protected Class<FileRegion> getMessageClass() {
/* 59 */     return FileRegion.class;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected IoBuffer getNextBuffer(FileRegion fileRegion) throws IOException {
/* 65 */     if (fileRegion.getRemainingBytes() <= 0L) {
/* 66 */       return null;
/*    */     }
/*    */ 
/*    */     
/* 70 */     int bufferSize = (int)Math.min(getWriteBufferSize(), fileRegion.getRemainingBytes());
/* 71 */     IoBuffer buffer = IoBuffer.allocate(bufferSize);
/*    */ 
/*    */     
/* 74 */     int bytesRead = fileRegion.getFileChannel().read(buffer.buf(), fileRegion.getPosition());
/* 75 */     fileRegion.update(bytesRead);
/*    */ 
/*    */     
/* 78 */     buffer.flip();
/* 79 */     return buffer;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/stream/FileRegionWriteFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */