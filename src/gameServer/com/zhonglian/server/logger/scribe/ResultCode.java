/*    */ package com.zhonglian.server.logger.scribe;
/*    */ 
/*    */ import org.apache.thrift.TEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ResultCode
/*    */   implements TEnum
/*    */ {
/* 11 */   OK(0), TRY_LATER(1);
/*    */   
/*    */   private final int value;
/*    */   
/*    */   ResultCode(int value) {
/* 16 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getValue() {
/* 23 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ResultCode findByValue(int value) {
/* 32 */     switch (value) {
/*    */       case 0:
/* 34 */         return OK;
/*    */       case 1:
/* 36 */         return TRY_LATER;
/*    */     } 
/* 38 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/logger/scribe/ResultCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */