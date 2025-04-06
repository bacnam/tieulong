/*    */ package com.zhonglian.server.common.utils;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CopyOnWriteArrayList;
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
/*    */ public class Lists
/*    */ {
/*    */   public static <E> List<E> newConcurrentList() {
/* 25 */     return new CopyOnWriteArrayList<>();
/*    */   }
/*    */   
/*    */   public static <E> List<E> newConcurrentList(Collection<? extends E> c) {
/* 29 */     return new CopyOnWriteArrayList<>(c);
/*    */   }
/*    */   
/*    */   public static <E> ArrayList<E> newArrayList() {
/* 33 */     return new ArrayList<>();
/*    */   }
/*    */   
/*    */   public static <E> ArrayList<E> newArrayList(Collection<? extends E> c) {
/* 37 */     return new ArrayList<>(c);
/*    */   }
/*    */   
/*    */   public static <E> LinkedList<E> newLinkedList() {
/* 41 */     return new LinkedList<>();
/*    */   }
/*    */   
/*    */   public static <E> LinkedList<E> newLinkedList(Collection<? extends E> c) {
/* 45 */     return new LinkedList<>(c);
/*    */   }
/*    */   
/*    */   public static <E> HashSet<E> newHashSet() {
/* 49 */     return new HashSet<>();
/*    */   }
/*    */   
/*    */   public static <E> HashSet<E> newHashSet(Collection<? extends E> c) {
/* 53 */     return new HashSet<>(c);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/Lists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */