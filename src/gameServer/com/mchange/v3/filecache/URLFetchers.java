/*     */ package com.mchange.v3.filecache;
/*     */ 
/*     */ import com.mchange.v1.io.InputStreamUtils;
/*     */ import com.mchange.v1.io.ReaderUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.StringWriter;
/*     */ import java.net.URL;
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
/*     */ public enum URLFetchers
/*     */   implements URLFetcher
/*     */ {
/*  47 */   DEFAULT
/*     */   {
/*     */     public InputStream openStream(URL param1URL, MLogger param1MLogger) throws IOException {
/*  50 */       return param1URL.openStream();
/*     */     }
/*     */   },
/*  53 */   BUFFERED_WGET
/*     */   {
/*     */     public InputStream openStream(URL param1URL, MLogger param1MLogger) throws IOException
/*     */     {
/*  57 */       Process process = (new ProcessBuilder(new String[] { "wget", "-O", "-", param1URL.toString() })).start();
/*     */       
/*  59 */       BufferedInputStream bufferedInputStream = null;
/*     */ 
/*     */       
/*     */       try {
/*  63 */         bufferedInputStream = new BufferedInputStream(process.getInputStream(), 1048576);
/*  64 */         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1048576);
/*     */         int i;
/*  66 */         for (i = bufferedInputStream.read(); i >= 0; i = bufferedInputStream.read()) {
/*  67 */           byteArrayOutputStream.write(i);
/*     */         }
/*  69 */         return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
/*     */       }
/*     */       finally {
/*     */         
/*  73 */         InputStreamUtils.attemptClose(bufferedInputStream);
/*     */         
/*  75 */         if (param1MLogger.isLoggable(MLevel.FINER)) {
/*     */           
/*  77 */           BufferedReader bufferedReader = null;
/*     */ 
/*     */           
/*     */           try {
/*  81 */             bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()), 1048576);
/*  82 */             StringWriter stringWriter = new StringWriter(1048576);
/*     */             int i;
/*  84 */             for (i = bufferedReader.read(); i >= 0; i = bufferedReader.read()) {
/*  85 */               stringWriter.write(i);
/*     */             }
/*  87 */             param1MLogger.log(MLevel.FINER, "wget error stream for '" + param1URL + "':\n " + stringWriter.toString());
/*     */           } finally {
/*     */             
/*  90 */             ReaderUtils.attemptClose(bufferedReader);
/*     */           } 
/*     */         } 
/*     */         
/*     */         try {
/*  95 */           int i = process.waitFor();
/*  96 */           if (i != 0) {
/*  97 */             throw new IOException("wget process terminated abnormally [return code: " + i + "]");
/*     */           }
/*  99 */         } catch (InterruptedException interruptedException) {
/*     */           
/* 101 */           if (param1MLogger.isLoggable(MLevel.FINER)) {
/* 102 */             param1MLogger.log(MLevel.FINER, "InterruptedException while waiting for wget to complete.", interruptedException);
/*     */           }
/* 104 */           throw new IOException("Interrupted while waiting for wget to complete: " + interruptedException);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   };
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v3/filecache/URLFetchers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */