/*    */ package com.mchange.lang;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
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
/*    */ public class PotentiallySecondaryRuntimeException
/*    */   extends RuntimeException
/*    */   implements PotentiallySecondary
/*    */ {
/*    */   static final String NESTED_MSG = ">>>>>>>>>> NESTED EXCEPTION >>>>>>>>";
/*    */   Throwable nested;
/*    */   
/*    */   public PotentiallySecondaryRuntimeException(String paramString, Throwable paramThrowable) {
/* 48 */     super(paramString);
/* 49 */     this.nested = paramThrowable;
/*    */   }
/*    */   
/*    */   public PotentiallySecondaryRuntimeException(Throwable paramThrowable) {
/* 53 */     this("", paramThrowable);
/*    */   }
/*    */   public PotentiallySecondaryRuntimeException(String paramString) {
/* 56 */     this(paramString, null);
/*    */   }
/*    */   public PotentiallySecondaryRuntimeException() {
/* 59 */     this("", null);
/*    */   }
/*    */   public Throwable getNestedThrowable() {
/* 62 */     return this.nested;
/*    */   }
/*    */   
/*    */   public void printStackTrace(PrintWriter paramPrintWriter) {
/* 66 */     super.printStackTrace(paramPrintWriter);
/* 67 */     if (this.nested != null) {
/*    */       
/* 69 */       paramPrintWriter.println(">>>>>>>>>> NESTED EXCEPTION >>>>>>>>");
/* 70 */       this.nested.printStackTrace(paramPrintWriter);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void printStackTrace(PrintStream paramPrintStream) {
/* 76 */     super.printStackTrace(paramPrintStream);
/* 77 */     if (this.nested != null) {
/*    */       
/* 79 */       paramPrintStream.println("NESTED_MSG");
/* 80 */       this.nested.printStackTrace(paramPrintStream);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void printStackTrace() {
/* 85 */     printStackTrace(System.err);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/PotentiallySecondaryRuntimeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */