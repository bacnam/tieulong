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
/*    */ public class StartsWithFilenameFilter
/*    */   implements FilenameFilter
/*    */ {
/*    */   public static final int ALWAYS = 0;
/*    */   public static final int NEVER = 1;
/*    */   public static final int MATCH = 2;
/* 46 */   String[] beginnings = null;
/*    */   
/*    */   int accept_dirs;
/*    */   
/*    */   public StartsWithFilenameFilter(String[] paramArrayOfString, int paramInt) {
/* 51 */     this.beginnings = paramArrayOfString;
/* 52 */     this.accept_dirs = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public StartsWithFilenameFilter(String paramString, int paramInt) {
/* 57 */     this.beginnings = new String[] { paramString };
/* 58 */     this.accept_dirs = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File paramFile, String paramString) {
/* 63 */     if (this.accept_dirs != 2 && (new File(paramFile, paramString)).isDirectory()) return (this.accept_dirs == 0); 
/* 64 */     for (int i = this.beginnings.length; --i >= 0;) {
/* 65 */       if (paramString.startsWith(this.beginnings[i])) return true; 
/* 66 */     }  return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/impl/StartsWithFilenameFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */