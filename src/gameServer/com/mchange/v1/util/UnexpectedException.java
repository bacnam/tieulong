/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import com.mchange.lang.PotentiallySecondaryRuntimeException;
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
/*    */ public class UnexpectedException
/*    */   extends PotentiallySecondaryRuntimeException
/*    */ {
/*    */   public UnexpectedException(String paramString, Throwable paramThrowable) {
/* 43 */     super(paramString, paramThrowable);
/*    */   }
/*    */   public UnexpectedException(Throwable paramThrowable) {
/* 46 */     super(paramThrowable);
/*    */   }
/*    */   public UnexpectedException(String paramString) {
/* 49 */     super(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public UnexpectedException() {}
/*    */   
/*    */   public UnexpectedException(Throwable paramThrowable, String paramString) {
/* 56 */     this(paramString, paramThrowable);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/UnexpectedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */