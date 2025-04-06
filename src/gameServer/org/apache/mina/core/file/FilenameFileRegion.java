/*    */ package org.apache.mina.core.file;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.FileChannel;
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
/*    */ public class FilenameFileRegion
/*    */   extends DefaultFileRegion
/*    */ {
/*    */   private final File file;
/*    */   
/*    */   public FilenameFileRegion(File file, FileChannel channel) throws IOException {
/* 38 */     this(file, channel, 0L, file.length());
/*    */   }
/*    */   
/*    */   public FilenameFileRegion(File file, FileChannel channel, long position, long remainingBytes) {
/* 42 */     super(channel, position, remainingBytes);
/*    */     
/* 44 */     if (file == null) {
/* 45 */       throw new IllegalArgumentException("file can not be null");
/*    */     }
/* 47 */     this.file = file;
/*    */   }
/*    */   
/*    */   public String getFilename() {
/* 51 */     return this.file.getAbsolutePath();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/file/FilenameFileRegion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */