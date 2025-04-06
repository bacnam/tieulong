/*    */ package com.mchange.v2.collection;
/*    */ 
/*    */ import com.mchange.v2.lang.ObjectUtils;
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
/*    */ public class MapEntry
/*    */   implements Map.Entry
/*    */ {
/*    */   Object key;
/*    */   Object value;
/*    */   
/*    */   public MapEntry(Object paramObject1, Object paramObject2) {
/* 48 */     this.key = paramObject1;
/* 49 */     this.value = paramObject2;
/*    */   }
/*    */   
/*    */   public Object getKey() {
/* 53 */     return this.key;
/*    */   }
/*    */   public Object getValue() {
/* 56 */     return this.value;
/*    */   }
/*    */   public Object setValue(Object paramObject) {
/* 59 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 63 */     if (paramObject instanceof Map.Entry) {
/*    */       
/* 65 */       Map.Entry entry = (Map.Entry)paramObject;
/* 66 */       return (ObjectUtils.eqOrBothNull(this.key, entry.getKey()) && ObjectUtils.eqOrBothNull(this.value, entry.getValue()));
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 72 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 80 */     return ObjectUtils.hashOrZero(this.key) ^ ObjectUtils.hashOrZero(this.value);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/collection/MapEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */