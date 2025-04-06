/*    */ package com.mchange.io;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
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
/*    */ public final class FileUtils
/*    */ {
/*    */   public static byte[] getBytes(File paramFile, int paramInt) throws IOException {
/* 44 */     BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
/*    */     try {
/* 46 */       return InputStreamUtils.getBytes(bufferedInputStream, paramInt);
/*    */     } finally {
/* 48 */       InputStreamUtils.attemptClose(bufferedInputStream);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static byte[] getBytes(File paramFile) throws IOException {
/* 53 */     BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
/*    */     try {
/* 55 */       return InputStreamUtils.getBytes(bufferedInputStream);
/*    */     } finally {
/* 57 */       InputStreamUtils.attemptClose(bufferedInputStream);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getContentsAsString(File paramFile, String paramString) throws IOException, UnsupportedEncodingException {
/* 63 */     BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
/*    */     try {
/* 65 */       return InputStreamUtils.getContentsAsString(bufferedInputStream, paramString);
/*    */     } finally {
/* 67 */       InputStreamUtils.attemptClose(bufferedInputStream);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getContentsAsString(File paramFile) throws IOException {
/* 74 */     BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
/*    */     try {
/* 76 */       return InputStreamUtils.getContentsAsString(bufferedInputStream);
/*    */     } finally {
/* 78 */       InputStreamUtils.attemptClose(bufferedInputStream);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getContentsAsString(File paramFile, int paramInt, String paramString) throws IOException, UnsupportedEncodingException {
/* 84 */     BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
/*    */     try {
/* 86 */       return InputStreamUtils.getContentsAsString(bufferedInputStream, paramInt, paramString);
/*    */     } finally {
/* 88 */       InputStreamUtils.attemptClose(bufferedInputStream);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getContentsAsString(File paramFile, int paramInt) throws IOException {
/* 94 */     BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramFile));
/*    */     try {
/* 96 */       return InputStreamUtils.getContentsAsString(bufferedInputStream, paramInt);
/*    */     } finally {
/* 98 */       InputStreamUtils.attemptClose(bufferedInputStream);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/FileUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */