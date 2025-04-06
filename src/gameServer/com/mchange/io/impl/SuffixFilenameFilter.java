/*    */ package com.mchange.io.impl;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FilenameFilter;
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
/*    */ public class SuffixFilenameFilter
/*    */   implements FilenameFilter
/*    */ {
/*    */   public static final int ALWAYS = 0;
/*    */   public static final int NEVER = 1;
/*    */   public static final int MATCH = 2;
/* 46 */   String[] suffixes = null;
/*    */   
/*    */   int accept_dirs;
/*    */   
/*    */   public SuffixFilenameFilter(String[] paramArrayOfString, int paramInt) {
/* 51 */     this.suffixes = paramArrayOfString;
/* 52 */     this.accept_dirs = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public SuffixFilenameFilter(String paramString, int paramInt) {
/* 57 */     this.suffixes = new String[] { paramString };
/* 58 */     this.accept_dirs = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File paramFile, String paramString) {
/* 63 */     if (this.accept_dirs != 2 && (new File(paramFile, paramString)).isDirectory()) return (this.accept_dirs == 0); 
/* 64 */     for (int i = this.suffixes.length; --i >= 0;) {
/* 65 */       if (paramString.endsWith(this.suffixes[i])) return true; 
/* 66 */     }  return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/impl/SuffixFilenameFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */