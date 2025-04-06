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
/*    */ class BSHThrowStatement
/*    */   extends SimpleNode
/*    */ {
/*    */   BSHThrowStatement(int id) {
/* 39 */     super(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 44 */     Object obj = ((SimpleNode)jjtGetChild(0)).eval(callstack, interpreter);
/*    */ 
/*    */ 
/*    */     
/* 48 */     if (!(obj instanceof Exception)) {
/* 49 */       throw new EvalError("Expression in 'throw' must be Exception type", this, callstack);
/*    */     }
/*    */ 
/*    */     
/* 53 */     throw new TargetError((Exception)obj, this, callstack);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHThrowStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */