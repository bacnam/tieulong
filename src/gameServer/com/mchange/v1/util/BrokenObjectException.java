/*    */ package com.mchange.v1.util;
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
/*    */ public class BrokenObjectException
/*    */   extends Exception
/*    */ {
/*    */   Object broken;
/*    */   
/*    */   public BrokenObjectException(Object paramObject, String paramString) {
/* 44 */     super(paramString);
/* 45 */     this.broken = paramObject;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BrokenObjectException(Object paramObject) {
/* 51 */     this.broken = paramObject;
/*    */   }
/*    */   
/*    */   public Object getBrokenObject() {
/* 55 */     return this.broken;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/BrokenObjectException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */