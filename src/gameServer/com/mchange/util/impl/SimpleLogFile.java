/*    */ package com.mchange.util.impl;
/*    */ 
/*    */ import com.mchange.util.MessageLogger;
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.text.DateFormat;
/*    */ import java.util.Date;
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
/*    */ public class SimpleLogFile
/*    */   implements MessageLogger
/*    */ {
/*    */   PrintWriter logWriter;
/* 47 */   DateFormat df = DateFormat.getDateTimeInstance(3, 3);
/*    */ 
/*    */   
/*    */   public SimpleLogFile(File paramFile, String paramString) throws UnsupportedEncodingException, IOException {
/* 51 */     this.logWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(paramFile.getAbsolutePath(), true), paramString)), true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleLogFile(File paramFile) throws IOException {
/* 59 */     this.logWriter = new PrintWriter(new BufferedOutputStream(new FileOutputStream(paramFile.getAbsolutePath(), true)), true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void log(String paramString) throws IOException {
/* 66 */     logMessage(paramString);
/* 67 */     flush();
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void log(Throwable paramThrowable, String paramString) throws IOException {
/* 72 */     logMessage(paramString);
/* 73 */     paramThrowable.printStackTrace(this.logWriter);
/* 74 */     flush();
/*    */   }
/*    */   
/*    */   private void logMessage(String paramString) {
/* 78 */     this.logWriter.println(this.df.format(new Date()) + " -- " + paramString);
/*    */   }
/*    */   private void flush() {
/* 81 */     this.logWriter.flush();
/*    */   }
/*    */   public synchronized void close() {
/* 84 */     this.logWriter.close();
/*    */   }
/*    */   public void finalize() {
/* 87 */     close();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/SimpleLogFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */