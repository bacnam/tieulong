/*    */ package com.mchange.v2.codegen.bean;
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
/*    */ class PropertyComparator
/*    */ {
/*    */   public int compare(Object paramObject1, Object paramObject2) {
/* 42 */     Property property1 = (Property)paramObject1;
/* 43 */     Property property2 = (Property)paramObject2;
/*    */     
/* 45 */     return property1.getName().compareTo(property2.getName());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/PropertyComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */