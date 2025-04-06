/*    */ package com.mchange.io.impl;
/*    */ 
/*    */ import com.mchange.io.ReadOnlyMemoryFile;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
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
/*    */ public class LazyReadOnlyMemoryFileImpl
/*    */   implements ReadOnlyMemoryFile
/*    */ {
/*    */   File file;
/* 44 */   byte[] bytes = null;
/* 45 */   long last_mod = -1L;
/* 46 */   int last_len = -1;
/*    */   
/*    */   public LazyReadOnlyMemoryFileImpl(File paramFile) {
/* 49 */     this.file = paramFile;
/*    */   }
/*    */   public LazyReadOnlyMemoryFileImpl(String paramString) {
/* 52 */     this(new File(paramString));
/*    */   }
/*    */   public File getFile() {
/* 55 */     return this.file;
/*    */   }
/*    */   
/*    */   public synchronized byte[] getBytes() throws IOException {
/* 59 */     update();
/* 60 */     return this.bytes;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void update() throws IOException {
/* 66 */     if (this.file.lastModified() > this.last_mod) {
/*    */       
/* 68 */       if (this.bytes != null)
/* 69 */         this.last_len = this.bytes.length; 
/* 70 */       refreshBytes();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void refreshBytes() throws IOException {
/* 77 */     ByteArrayOutputStream byteArrayOutputStream = (this.last_len > 0) ? new ByteArrayOutputStream(2 * this.last_len) : new ByteArrayOutputStream();
/*    */ 
/*    */ 
/*    */     
/* 81 */     BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.file));
/*    */     
/* 83 */     for (int i = bufferedInputStream.read(); i >= 0; ) { byteArrayOutputStream.write((byte)i); i = bufferedInputStream.read(); }
/* 84 */      this.bytes = byteArrayOutputStream.toByteArray();
/* 85 */     this.last_mod = this.file.lastModified();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/impl/LazyReadOnlyMemoryFileImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */