/*    */ package bsh;
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
/*    */ public class UtilEvalError
/*    */   extends Exception
/*    */ {
/*    */   protected UtilEvalError() {}
/*    */   
/*    */   public UtilEvalError(String s) {
/* 65 */     super(s);
/*    */   }
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
/*    */   public EvalError toEvalError(String msg, SimpleNode node, CallStack callstack) {
/* 78 */     if (Interpreter.DEBUG) {
/* 79 */       printStackTrace();
/*    */     }
/* 81 */     if (msg == null) {
/* 82 */       msg = "";
/*    */     } else {
/* 84 */       msg = msg + ": ";
/* 85 */     }  return new EvalError(msg + getMessage(), node, callstack);
/*    */   }
/*    */ 
/*    */   
/*    */   public EvalError toEvalError(SimpleNode node, CallStack callstack) {
/* 90 */     return toEvalError((String)null, node, callstack);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/UtilEvalError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */