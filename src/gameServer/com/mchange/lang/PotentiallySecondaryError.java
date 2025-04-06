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
/*    */ public class PotentiallySecondaryError
/*    */   extends Error
/*    */   implements PotentiallySecondary
/*    */ {
/*    */   static final String NESTED_MSG = ">>>>>>>>>> NESTED THROWABLE >>>>>>>>";
/*    */   Throwable nested;
/*    */   
/*    */   public PotentiallySecondaryError(String paramString, Throwable paramThrowable) {
/* 48 */     super(paramString);
/* 49 */     this.nested = paramThrowable;
/*    */   }
/*    */   
/*    */   public PotentiallySecondaryError(Throwable paramThrowable) {
/* 53 */     this("", paramThrowable);
/*    */   }
/*    */   public PotentiallySecondaryError(String paramString) {
/* 56 */     this(paramString, null);
/*    */   }
/*    */   public PotentiallySecondaryError() {
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
/* 69 */       paramPrintWriter.println(">>>>>>>>>> NESTED THROWABLE >>>>>>>>");
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/PotentiallySecondaryError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */