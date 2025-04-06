/*    */ package com.mchange.v2.util;
/*    */ 
/*    */ import com.mchange.v2.lang.VersionUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceClosedException
/*    */   extends RuntimeException
/*    */ {
/*    */   Throwable rootCause;
/*    */   
/*    */   public ResourceClosedException(String paramString, Throwable paramThrowable) {
/* 54 */     super(paramString);
/* 55 */     setRootCause(paramThrowable);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ResourceClosedException(Throwable paramThrowable) {
/* 61 */     setRootCause(paramThrowable);
/*    */   }
/*    */   
/*    */   public ResourceClosedException(String paramString) {
/* 65 */     super(paramString);
/*    */   }
/*    */   
/*    */   public ResourceClosedException() {}
/*    */   
/*    */   public Throwable getCause() {
/* 71 */     return this.rootCause;
/*    */   }
/*    */   
/*    */   private void setRootCause(Throwable paramThrowable) {
/* 75 */     this.rootCause = paramThrowable;
/* 76 */     if (VersionUtils.isAtLeastJavaVersion14())
/* 77 */       initCause(paramThrowable); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/util/ResourceClosedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */