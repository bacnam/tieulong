/*     */ package bsh;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ public class TargetError
/*     */   extends EvalError
/*     */ {
/*     */   Throwable target;
/*     */   boolean inNativeCode;
/*     */   
/*     */   public TargetError(String msg, Throwable t, SimpleNode node, CallStack callstack, boolean inNativeCode) {
/*  61 */     super(msg, node, callstack);
/*  62 */     this.target = t;
/*  63 */     this.inNativeCode = inNativeCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public TargetError(Throwable t, SimpleNode node, CallStack callstack) {
/*  68 */     this("TargetError", t, node, callstack, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getTarget() {
/*  74 */     if (this.target instanceof InvocationTargetException) {
/*  75 */       return ((InvocationTargetException)this.target).getTargetException();
/*     */     }
/*  77 */     return this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  82 */     return super.toString() + "\nTarget exception: " + printTargetError(this.target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void printStackTrace() {
/*  88 */     printStackTrace(false, System.err);
/*     */   }
/*     */   
/*     */   public void printStackTrace(PrintStream out) {
/*  92 */     printStackTrace(false, out);
/*     */   }
/*     */   
/*     */   public void printStackTrace(boolean debug, PrintStream out) {
/*  96 */     if (debug) {
/*  97 */       super.printStackTrace(out);
/*  98 */       out.println("--- Target Stack Trace ---");
/*     */     } 
/* 100 */     this.target.printStackTrace(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String printTargetError(Throwable t) {
/* 110 */     String s = this.target.toString();
/*     */     
/* 112 */     if (Capabilities.canGenerateInterfaces()) {
/* 113 */       s = s + "\n" + xPrintTargetError(t);
/*     */     }
/* 115 */     return s;
/*     */   }
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
/*     */   public String xPrintTargetError(Throwable t) {
/* 129 */     String getTarget = "import java.lang.reflect.UndeclaredThrowableException;String result=\"\";while ( target instanceof UndeclaredThrowableException ) {\ttarget=target.getUndeclaredThrowable(); \tresult+=\"Nested: \"+target.toString();}return result;";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     Interpreter i = new Interpreter();
/*     */     try {
/* 139 */       i.set("target", t);
/* 140 */       return (String)i.eval(getTarget);
/* 141 */     } catch (EvalError e) {
/* 142 */       throw new InterpreterError("xprintarget: " + e.toString());
/*     */     } 
/*     */   }
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
/*     */   public boolean inNativeCode() {
/* 156 */     return this.inNativeCode;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/TargetError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */