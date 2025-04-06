/*    */ package com.mchange.io.impl;
/*    */ 
/*    */ import com.mchange.io.StringMemoryFile;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.UnsupportedEncodingException;
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
/*    */ public class LazyStringMemoryFileImpl
/*    */   extends LazyReadOnlyMemoryFileImpl
/*    */   implements StringMemoryFile
/*    */ {
/*    */   private static final String DEFAULT_ENCODING;
/*    */   
/*    */   static {
/* 49 */     String str = System.getProperty("file.encoding");
/* 50 */     DEFAULT_ENCODING = (str == null) ? "8859_1" : str;
/*    */   }
/*    */ 
/*    */   
/* 54 */   String encoding = null;
/* 55 */   String string = null;
/*    */   
/*    */   public LazyStringMemoryFileImpl(File paramFile) {
/* 58 */     super(paramFile);
/*    */   }
/*    */   public LazyStringMemoryFileImpl(String paramString) {
/* 61 */     super(paramString);
/*    */   }
/*    */   
/*    */   public synchronized String asString(String paramString) throws IOException, UnsupportedEncodingException {
/* 65 */     update();
/* 66 */     if (this.encoding != paramString)
/* 67 */       this.string = new String(this.bytes, paramString); 
/* 68 */     return this.string;
/*    */   }
/*    */ 
/*    */   
/*    */   public String asString() throws IOException {
/*    */     try {
/* 74 */       return asString(DEFAULT_ENCODING);
/* 75 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/* 76 */       throw new InternalError("Default Encoding is not supported?!");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   void refreshBytes() throws IOException {
/* 82 */     super.refreshBytes();
/* 83 */     this.encoding = this.string = null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/impl/LazyStringMemoryFileImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */