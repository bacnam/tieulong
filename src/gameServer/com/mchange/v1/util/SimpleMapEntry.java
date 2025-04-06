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
/*    */ public class SimpleMapEntry
/*    */   extends AbstractMapEntry
/*    */   implements Map.Entry
/*    */ {
/*    */   Object key;
/*    */   Object value;
/*    */   
/*    */   public SimpleMapEntry(Object paramObject1, Object paramObject2) {
/* 47 */     this.key = paramObject1;
/* 48 */     this.value = paramObject2;
/*    */   }
/*    */   
/*    */   public Object getKey() {
/* 52 */     return this.key;
/*    */   }
/*    */   public Object getValue() {
/* 55 */     return this.value;
/*    */   }
/*    */   
/*    */   public Object setValue(Object paramObject) {
/* 59 */     Object object = paramObject;
/* 60 */     this.value = paramObject;
/* 61 */     return object;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/SimpleMapEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */