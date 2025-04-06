/*    */ package com.mchange.lang;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ArrayUtils
/*    */ {
/*    */   public static int indexOf(Object[] paramArrayOfObject, Object paramObject) {
/*    */     byte b;
/*    */     int i;
/* 51 */     for (b = 0, i = paramArrayOfObject.length; b < i; b++) {
/* 52 */       if (paramObject.equals(paramArrayOfObject[b])) return b; 
/* 53 */     }  return -1;
/*    */   }
/*    */   public static int identityIndexOf(Object[] paramArrayOfObject, Object paramObject) {
/*    */     byte b;
/*    */     int i;
/* 58 */     for (b = 0, i = paramArrayOfObject.length; b < i; b++) {
/* 59 */       if (paramObject == paramArrayOfObject[b]) return b; 
/* 60 */     }  return -1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int hashAll(Object[] paramArrayOfObject) {
/* 71 */     int i = 0; byte b; int j;
/* 72 */     for (b = 0, j = paramArrayOfObject.length; b < j; b++) {
/*    */       
/* 74 */       Object object = paramArrayOfObject[b];
/* 75 */       if (object != null) i ^= object.hashCode(); 
/*    */     } 
/* 77 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int hashAll(int[] paramArrayOfint) {
/* 82 */     int i = 0; byte b; int j;
/* 83 */     for (b = 0, j = paramArrayOfint.length; b < j; b++)
/* 84 */       i ^= paramArrayOfint[b]; 
/* 85 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean startsWith(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 90 */     int i = paramArrayOfbyte1.length;
/* 91 */     int j = paramArrayOfbyte2.length;
/* 92 */     if (i < j)
/* 93 */       return false; 
/* 94 */     for (byte b = 0; b < j; b++) {
/* 95 */       if (paramArrayOfbyte1[b] != paramArrayOfbyte2[b])
/* 96 */         return false; 
/* 97 */     }  return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/ArrayUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */