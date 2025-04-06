/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.recovery.ResilientFileOutputStream;
/*     */ import ch.qos.logback.core.util.FileUtil;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
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
/*     */ public class FileAppender<E>
/*     */   extends OutputStreamAppender<E>
/*     */ {
/*     */   protected boolean append = true;
/*  44 */   protected String fileName = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean prudent = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(String file) {
/*  53 */     if (file == null) {
/*  54 */       this.fileName = file;
/*     */     }
/*     */     else {
/*     */       
/*  58 */       this.fileName = file.trim();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAppend() {
/*  66 */     return this.append;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String rawFileProperty() {
/*  76 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFile() {
/*  87 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  96 */     int errors = 0;
/*  97 */     if (getFile() != null) {
/*  98 */       addInfo("File property is set to [" + this.fileName + "]");
/*     */       
/* 100 */       if (this.prudent && 
/* 101 */         !isAppend()) {
/* 102 */         setAppend(true);
/* 103 */         addWarn("Setting \"Append\" property to true on account of \"Prudent\" mode");
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 108 */         openFile(getFile());
/* 109 */       } catch (IOException e) {
/* 110 */         errors++;
/* 111 */         addError("openFile(" + this.fileName + "," + this.append + ") call failed.", e);
/*     */       } 
/*     */     } else {
/* 114 */       errors++;
/* 115 */       addError("\"File\" property not set for appender named [" + this.name + "].");
/*     */     } 
/* 117 */     if (errors == 0) {
/* 118 */       super.start();
/*     */     }
/*     */   }
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
/*     */   public void openFile(String file_name) throws IOException {
/* 139 */     this.lock.lock();
/*     */     try {
/* 141 */       File file = new File(file_name);
/* 142 */       boolean result = FileUtil.createMissingParentDirectories(file);
/* 143 */       if (!result) {
/* 144 */         addError("Failed to create parent directories for [" + file.getAbsolutePath() + "]");
/*     */       }
/*     */ 
/*     */       
/* 148 */       ResilientFileOutputStream resilientFos = new ResilientFileOutputStream(file, this.append);
/*     */       
/* 150 */       resilientFos.setContext(this.context);
/* 151 */       setOutputStream((OutputStream)resilientFos);
/*     */     } finally {
/* 153 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrudent() {
/* 163 */     return this.prudent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrudent(boolean prudent) {
/* 173 */     this.prudent = prudent;
/*     */   }
/*     */   
/*     */   public void setAppend(boolean append) {
/* 177 */     this.append = append;
/*     */   }
/*     */   
/*     */   private void safeWrite(E event) throws IOException {
/* 181 */     ResilientFileOutputStream resilientFOS = (ResilientFileOutputStream)getOutputStream();
/* 182 */     FileChannel fileChannel = resilientFOS.getChannel();
/* 183 */     if (fileChannel == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 188 */     boolean interrupted = Thread.interrupted();
/*     */     
/* 190 */     FileLock fileLock = null;
/*     */     try {
/* 192 */       fileLock = fileChannel.lock();
/* 193 */       long position = fileChannel.position();
/* 194 */       long size = fileChannel.size();
/* 195 */       if (size != position) {
/* 196 */         fileChannel.position(size);
/*     */       }
/* 198 */       super.writeOut(event);
/* 199 */     } catch (IOException e) {
/*     */       
/* 201 */       resilientFOS.postIOFailure(e);
/*     */     } finally {
/*     */       
/* 204 */       if (fileLock != null && fileLock.isValid()) {
/* 205 */         fileLock.release();
/*     */       }
/*     */ 
/*     */       
/* 209 */       if (interrupted) {
/* 210 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeOut(E event) throws IOException {
/* 217 */     if (this.prudent) {
/* 218 */       safeWrite(event);
/*     */     } else {
/* 220 */       super.writeOut(event);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/FileAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */