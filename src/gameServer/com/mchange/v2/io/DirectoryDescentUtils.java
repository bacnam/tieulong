/*     */ package com.mchange.v2.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
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
/*     */ public final class DirectoryDescentUtils
/*     */ {
/*     */   public static FileIterator depthFirstEagerDescent(File paramFile) throws IOException {
/*  48 */     return depthFirstEagerDescent(paramFile, null, false);
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
/*     */   public static FileIterator depthFirstEagerDescent(File paramFile, FileFilter paramFileFilter, boolean paramBoolean) throws IOException {
/*  61 */     LinkedList linkedList = new LinkedList();
/*  62 */     HashSet hashSet = new HashSet();
/*  63 */     depthFirstEagerDescend(paramFile, paramFileFilter, paramBoolean, linkedList, hashSet);
/*  64 */     return new IteratorFileIterator(linkedList.iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addSubtree(File paramFile, FileFilter paramFileFilter, boolean paramBoolean, Collection paramCollection) throws IOException {
/*  69 */     HashSet hashSet = new HashSet();
/*  70 */     depthFirstEagerDescend(paramFile, paramFileFilter, paramBoolean, paramCollection, hashSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void depthFirstEagerDescend(File paramFile, FileFilter paramFileFilter, boolean paramBoolean, Collection<File> paramCollection, Set<String> paramSet) throws IOException {
/*  77 */     String str = paramFile.getCanonicalPath();
/*  78 */     if (!paramSet.contains(str)) {
/*     */       
/*  80 */       if (paramFileFilter == null || paramFileFilter.accept(paramFile))
/*  81 */         paramCollection.add(paramBoolean ? new File(str) : paramFile); 
/*  82 */       paramSet.add(str);
/*  83 */       String[] arrayOfString = paramFile.list(); byte b; int i;
/*  84 */       for (b = 0, i = arrayOfString.length; b < i; b++) {
/*     */         
/*  86 */         File file = new File(paramFile, arrayOfString[b]);
/*  87 */         if (file.isDirectory()) {
/*  88 */           depthFirstEagerDescend(file, paramFileFilter, paramBoolean, paramCollection, paramSet);
/*     */         }
/*  90 */         else if (paramFileFilter == null || paramFileFilter.accept(file)) {
/*  91 */           paramCollection.add(paramBoolean ? file.getCanonicalFile() : file);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class IteratorFileIterator implements FileIterator {
/*     */     Iterator ii;
/*     */     Object last;
/*     */     
/*     */     IteratorFileIterator(Iterator param1Iterator) {
/* 102 */       this.ii = param1Iterator;
/*     */     }
/*     */     public File nextFile() throws IOException {
/* 105 */       return (File)next();
/*     */     }
/*     */     public boolean hasNext() throws IOException {
/* 108 */       return this.ii.hasNext();
/*     */     }
/*     */     public Object next() throws IOException {
/* 111 */       return this.last = this.ii.next();
/*     */     }
/*     */     
/*     */     public void remove() throws IOException {
/* 115 */       if (this.last != null) {
/*     */         
/* 117 */         ((File)this.last).delete();
/* 118 */         this.last = null;
/*     */       } else {
/*     */         
/* 121 */         throw new IllegalStateException();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() throws IOException {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 135 */       FileIterator fileIterator = depthFirstEagerDescent(new File(paramArrayOfString[0]));
/* 136 */       while (fileIterator.hasNext()) {
/* 137 */         System.err.println(fileIterator.nextFile().getPath());
/*     */       }
/* 139 */     } catch (Exception exception) {
/* 140 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/io/DirectoryDescentUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */