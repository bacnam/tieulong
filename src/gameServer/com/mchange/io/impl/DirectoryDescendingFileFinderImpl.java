/*     */ package com.mchange.io.impl;
/*     */ 
/*     */ import com.mchange.io.FileEnumeration;
/*     */ import com.mchange.io.IOEnumeration;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Stack;
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
/*     */ public class DirectoryDescendingFileFinderImpl
/*     */   implements IOEnumeration, FileEnumeration
/*     */ {
/*  47 */   private static final Object dummy = new Object();
/*     */   
/*  49 */   Hashtable markedDirex = new Hashtable<Object, Object>();
/*     */   
/*  51 */   Stack direx = new Stack();
/*  52 */   Stack files = new Stack();
/*     */ 
/*     */   
/*     */   FilenameFilter filter;
/*     */   
/*     */   boolean canonical;
/*     */ 
/*     */   
/*     */   public DirectoryDescendingFileFinderImpl(File paramFile, FilenameFilter paramFilenameFilter, boolean paramBoolean) throws IOException {
/*  61 */     if (!paramFile.isDirectory())
/*  62 */       throw new IllegalArgumentException(paramFile.getName() + " is not a directory."); 
/*  63 */     this.filter = paramFilenameFilter;
/*  64 */     this.canonical = paramBoolean;
/*  65 */     blossomDirectory(paramFile);
/*  66 */     while (this.files.empty() && !this.direx.empty())
/*  67 */       blossomDirectory(this.direx.pop()); 
/*     */   }
/*     */   
/*     */   public DirectoryDescendingFileFinderImpl(File paramFile) throws IOException {
/*  71 */     this(paramFile, null, false);
/*     */   }
/*     */   public boolean hasMoreFiles() {
/*  74 */     return !this.files.empty();
/*     */   }
/*     */   
/*     */   public File nextFile() throws IOException {
/*  78 */     if (this.files.empty()) throw new NoSuchElementException(); 
/*  79 */     File file = this.files.pop();
/*  80 */     while (this.files.empty() && !this.direx.empty())
/*  81 */       blossomDirectory(this.direx.pop()); 
/*  82 */     return file;
/*     */   }
/*     */   
/*     */   public boolean hasMoreElements() {
/*  86 */     return hasMoreFiles();
/*     */   }
/*     */   public Object nextElement() throws IOException {
/*  89 */     return nextFile();
/*     */   }
/*     */ 
/*     */   
/*     */   private void blossomDirectory(File paramFile) throws IOException {
/*  94 */     String str = paramFile.getCanonicalPath();
/*  95 */     String[] arrayOfString = (this.filter == null) ? paramFile.list() : paramFile.list(this.filter);
/*  96 */     for (int i = arrayOfString.length; --i >= 0;) {
/*     */ 
/*     */       
/*  99 */       if (this.filter == null || this.filter.accept(paramFile, arrayOfString[i])) {
/*     */         
/* 101 */         String str1 = (this.canonical ? str : paramFile.getPath()) + File.separator + arrayOfString[i];
/* 102 */         File file = new File(str1);
/*     */ 
/*     */         
/* 105 */         if (file.isFile()) { this.files.push(file);
/*     */           continue; }
/*     */         
/* 108 */         if (!this.markedDirex.containsKey(file.getCanonicalPath())) {
/* 109 */           this.direx.push(file);
/*     */         }
/*     */       } 
/*     */     } 
/* 113 */     this.markedDirex.put(str, dummy);
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
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 144 */       File file = new File(paramArrayOfString[0]);
/* 145 */       DirectoryDescendingFileFinderImpl directoryDescendingFileFinderImpl = new DirectoryDescendingFileFinderImpl(file);
/* 146 */       while (directoryDescendingFileFinderImpl.hasMoreFiles()) {
/* 147 */         System.out.println(directoryDescendingFileFinderImpl.nextFile().getAbsolutePath());
/*     */       }
/* 149 */     } catch (Exception exception) {
/* 150 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/impl/DirectoryDescendingFileFinderImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */