/*     */ package com.mchange.lang;
/*     */ 
/*     */ import com.mchange.v2.lang.VersionUtils;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PotentiallySecondaryException
/*     */   extends Exception
/*     */   implements PotentiallySecondary
/*     */ {
/*     */   static final String NESTED_MSG = ">>>>>>>>>> NESTED EXCEPTION >>>>>>>>";
/*     */   Throwable nested;
/*     */   
/*     */   public PotentiallySecondaryException(String paramString, Throwable paramThrowable) {
/*  53 */     super(paramString);
/*  54 */     this.nested = paramThrowable;
/*     */   }
/*     */   
/*     */   public PotentiallySecondaryException(Throwable paramThrowable) {
/*  58 */     this("", paramThrowable);
/*     */   }
/*     */   public PotentiallySecondaryException(String paramString) {
/*  61 */     this(paramString, null);
/*     */   }
/*     */   public PotentiallySecondaryException() {
/*  64 */     this("", null);
/*     */   }
/*     */   public Throwable getNestedThrowable() {
/*  67 */     return this.nested;
/*     */   }
/*     */   
/*     */   private void setNested(Throwable paramThrowable) {
/*  71 */     this.nested = paramThrowable;
/*  72 */     if (VersionUtils.isAtLeastJavaVersion14()) {
/*  73 */       initCause(paramThrowable);
/*     */     }
/*     */   }
/*     */   
/*     */   public void printStackTrace(PrintWriter paramPrintWriter) {
/*  78 */     super.printStackTrace(paramPrintWriter);
/*  79 */     if (!VersionUtils.isAtLeastJavaVersion14() && this.nested != null) {
/*     */       
/*  81 */       paramPrintWriter.println(">>>>>>>>>> NESTED EXCEPTION >>>>>>>>");
/*  82 */       this.nested.printStackTrace(paramPrintWriter);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintStream paramPrintStream) {
/*  88 */     super.printStackTrace(paramPrintStream);
/*  89 */     if (!VersionUtils.isAtLeastJavaVersion14() && this.nested != null) {
/*     */       
/*  91 */       paramPrintStream.println("NESTED_MSG");
/*  92 */       this.nested.printStackTrace(paramPrintStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace() {
/*  98 */     if (VersionUtils.isAtLeastJavaVersion14()) {
/*  99 */       super.printStackTrace();
/*     */     } else {
/* 101 */       printStackTrace(System.err);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/PotentiallySecondaryException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */