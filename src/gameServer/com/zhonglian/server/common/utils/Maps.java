/*    */ package com.zhonglian.server.common.utils;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Maps
/*    */ {
/*    */   public static <K, V> Map<K, V> list2Map(Function<? super V, ? extends K> keyMapper, List<V> list) {
/* 19 */     return (Map<K, V>)list.stream().collect(Collectors.toMap(keyMapper, p -> p));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <K, V> Map<K, V> newMap() {
/* 30 */     return new ConcurrentHashMap<>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <K, V> Map<K, V> newConcurrentMap() {
/* 41 */     ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
/*    */     
/* 43 */     return map;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <K, V> Map<K, V> newConcurrentHashMap() {
/* 54 */     ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
/*    */     
/* 56 */     return map;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/Maps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */