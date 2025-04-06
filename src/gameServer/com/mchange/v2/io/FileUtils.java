/*     */ package com.mchange.v2.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
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
/*     */ public final class FileUtils
/*     */ {
/*     */   public static File findRelativeToParent(File paramFile1, File paramFile2) throws IOException {
/*  44 */     String str1 = paramFile1.getPath();
/*  45 */     String str2 = paramFile2.getPath();
/*  46 */     if (!str2.startsWith(str1))
/*  47 */       throw new IllegalArgumentException(str2 + " is not a child of " + str1 + " [no transformations or canonicalizations tried]"); 
/*  48 */     String str3 = str2.substring(str1.length());
/*  49 */     File file = new File(str3);
/*  50 */     if (file.isAbsolute())
/*  51 */       file = new File(file.getPath().substring(1)); 
/*  52 */     return file;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long diskSpaceUsed(File paramFile) throws IOException {
/*  57 */     long l = 0L;
/*  58 */     for (FileIterator fileIterator = DirectoryDescentUtils.depthFirstEagerDescent(paramFile); fileIterator.hasNext(); ) {
/*     */       
/*  60 */       File file = fileIterator.nextFile();
/*     */       
/*  62 */       if (!file.isFile()) {
/*     */         continue;
/*     */       }
/*  65 */       l += file.length();
/*     */     } 
/*  67 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void touchExisting(File paramFile) throws IOException {
/*  72 */     if (paramFile.exists()) {
/*  73 */       unguardedTouch(paramFile);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void touch(File paramFile) throws IOException {
/*  78 */     if (!paramFile.exists())
/*  79 */       createEmpty(paramFile); 
/*  80 */     unguardedTouch(paramFile);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void createEmpty(File paramFile) throws IOException {
/*  85 */     RandomAccessFile randomAccessFile = null;
/*     */     
/*     */     try {
/*  88 */       randomAccessFile = new RandomAccessFile(paramFile, "rws");
/*  89 */       randomAccessFile.setLength(0L);
/*     */     } finally {
/*     */       
/*     */       try {
/*  93 */         if (randomAccessFile != null) randomAccessFile.close(); 
/*  94 */       } catch (IOException iOException) {
/*  95 */         iOException.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static void unguardedTouch(File paramFile) throws IOException {
/* 100 */     paramFile.setLastModified(System.currentTimeMillis());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/io/FileUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */