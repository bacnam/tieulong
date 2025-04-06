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
/*    */ public class UtilTargetError
/*    */   extends UtilEvalError
/*    */ {
/*    */   public Throwable t;
/*    */   
/*    */   public UtilTargetError(String message, Throwable t) {
/* 51 */     super(message);
/* 52 */     this.t = t;
/*    */   }
/*    */   
/*    */   public UtilTargetError(Throwable t) {
/* 56 */     this(null, t);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EvalError toEvalError(String msg, SimpleNode node, CallStack callstack) {
/* 65 */     if (msg == null) {
/* 66 */       msg = getMessage();
/*    */     } else {
/* 68 */       msg = msg + ": " + getMessage();
/*    */     } 
/* 70 */     return new TargetError(msg, this.t, node, callstack, false);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/UtilTargetError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */