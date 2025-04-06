/*    */ package com.zhonglian.server.common.utils;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class RquickSort {
/*    */   public int partition(List<Integer> list, int low, int high) {
/*  7 */     int small = low + 1;
/*    */     try {
/*  9 */       int pivot = Random.nextInt((high - low + 1 > 0) ? (high - low + 1) : 1) + low;
/* 10 */       int temp = ((Integer)list.get(low)).intValue();
/* 11 */       list.set(low, list.get(pivot));
/* 12 */       list.set(pivot, Integer.valueOf(temp));
/* 13 */       pivot = low;
/* 14 */       for (int i = small; i <= high; i++) {
/* 15 */         if (((Integer)list.get(i)).intValue() < ((Integer)list.get(pivot)).intValue()) {
/* 16 */           temp = ((Integer)list.get(small)).intValue();
/* 17 */           list.set(small, list.get(i));
/* 18 */           list.set(i, Integer.valueOf(temp));
/* 19 */           small++;
/*    */         } 
/*    */       } 
/*    */       
/* 23 */       temp = ((Integer)list.get(pivot)).intValue();
/* 24 */       list.set(pivot, list.get(small - 1));
/* 25 */       list.set(small - 1, Integer.valueOf(temp));
/* 26 */     } catch (Exception e) {
/* 27 */       System.out.println(high - low);
/*    */     } 
/* 29 */     return small - 1;
/*    */   }
/*    */   
/*    */   public void Rquicksort(List<Integer> list, int low, int high) {
/* 33 */     int pivot = partition(list, low, high);
/* 34 */     if (low < high) {
/* 35 */       Rquicksort(list, low, pivot);
/* 36 */       Rquicksort(list, pivot + 1, high);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/RquickSort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */