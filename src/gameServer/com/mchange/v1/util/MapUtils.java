/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ public final class MapUtils
/*    */ {
/*    */   public static boolean equivalentDisregardingSort(Map paramMap1, Map paramMap2) {
/* 49 */     if (paramMap1.size() != paramMap2.size()) {
/* 50 */       return false;
/*    */     }
/* 52 */     for (Object object : paramMap1.keySet()) {
/*    */ 
/*    */       
/* 55 */       if (!paramMap1.get(object).equals(paramMap2.get(object)))
/* 56 */         return false; 
/*    */     } 
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int hashContentsDisregardingSort(Map paramMap) {
/* 69 */     int i = 0;
/* 70 */     for (Object object : paramMap.keySet()) {
/*    */ 
/*    */       
/* 73 */       Object object1 = paramMap.get(object);
/* 74 */       i ^= object.hashCode() ^ object1.hashCode();
/*    */     } 
/* 76 */     return i;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/MapUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */