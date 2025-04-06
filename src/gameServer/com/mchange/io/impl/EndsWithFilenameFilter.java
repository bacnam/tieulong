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
/*    */ public class EndsWithFilenameFilter
/*    */   implements FilenameFilter
/*    */ {
/*    */   public static final int ALWAYS = 0;
/*    */   public static final int NEVER = 1;
/*    */   public static final int MATCH = 2;
/* 46 */   String[] endings = null;
/*    */   
/*    */   int accept_dirs;
/*    */   
/*    */   public EndsWithFilenameFilter(String[] paramArrayOfString, int paramInt) {
/* 51 */     this.endings = paramArrayOfString;
/* 52 */     this.accept_dirs = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public EndsWithFilenameFilter(String paramString, int paramInt) {
/* 57 */     this.endings = new String[] { paramString };
/* 58 */     this.accept_dirs = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File paramFile, String paramString) {
/* 63 */     if (this.accept_dirs != 2 && (new File(paramFile, paramString)).isDirectory()) return (this.accept_dirs == 0); 
/* 64 */     for (int i = this.endings.length; --i >= 0;) {
/* 65 */       if (paramString.endsWith(this.endings[i])) return true; 
/* 66 */     }  return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/impl/EndsWithFilenameFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */