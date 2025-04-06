/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ enum BstSide
/*    */ {
/* 26 */   LEFT
/*    */   {
/*    */     public BstSide other() {
/* 29 */       return RIGHT;
/*    */     }
/*    */   },
/* 32 */   RIGHT
/*    */   {
/*    */     public BstSide other() {
/* 35 */       return LEFT;
/*    */     }
/*    */   };
/*    */   
/*    */   abstract BstSide other();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/collect/BstSide.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */