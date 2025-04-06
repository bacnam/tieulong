/*    */ package com.facebook.fb303;
/*    */ 
/*    */ import org.apache.thrift.TEnum;
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
/*    */ public enum fb_status
/*    */   implements TEnum
/*    */ {
/* 17 */   DEAD(0),
/* 18 */   STARTING(1),
/* 19 */   ALIVE(2),
/* 20 */   STOPPING(3),
/* 21 */   STOPPED(4),
/* 22 */   WARNING(5);
/*    */   
/*    */   private final int value;
/*    */   
/*    */   fb_status(int value) {
/* 27 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getValue() {
/* 34 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static fb_status findByValue(int value) {
/* 42 */     switch (value) {
/*    */       case 0:
/* 44 */         return DEAD;
/*    */       case 1:
/* 46 */         return STARTING;
/*    */       case 2:
/* 48 */         return ALIVE;
/*    */       case 3:
/* 50 */         return STOPPING;
/*    */       case 4:
/* 52 */         return STOPPED;
/*    */       case 5:
/* 54 */         return WARNING;
/*    */     } 
/* 56 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/facebook/fb303/fb_status.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */