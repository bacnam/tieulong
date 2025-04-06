/*    */ package com.mchange.v1.util;
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
/*    */ public abstract class AbstractMapEntry
/*    */   implements Map.Entry
/*    */ {
/*    */   public abstract Object getKey();
/*    */   
/*    */   public abstract Object getValue();
/*    */   
/*    */   public abstract Object setValue(Object paramObject);
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 51 */     if (paramObject instanceof Map.Entry) {
/*    */       
/* 53 */       Map.Entry entry = (Map.Entry)paramObject;
/* 54 */       return (ObjectUtils.eqOrBothNull(getKey(), entry.getKey()) && ObjectUtils.eqOrBothNull(getValue(), entry.getValue()));
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 64 */     return ((getKey() == null) ? 0 : getKey().hashCode()) ^ ((getValue() == null) ? 0 : getValue().hashCode());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/AbstractMapEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */