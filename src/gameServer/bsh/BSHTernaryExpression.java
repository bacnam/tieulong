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
/*    */ class BSHTernaryExpression
/*    */   extends SimpleNode
/*    */ {
/*    */   BSHTernaryExpression(int id) {
/* 44 */     super(id);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 50 */     SimpleNode cond = (SimpleNode)jjtGetChild(0);
/* 51 */     SimpleNode evalTrue = (SimpleNode)jjtGetChild(1);
/* 52 */     SimpleNode evalFalse = (SimpleNode)jjtGetChild(2);
/*    */     
/* 54 */     if (BSHIfStatement.evaluateCondition(cond, callstack, interpreter)) {
/* 55 */       return evalTrue.eval(callstack, interpreter);
/*    */     }
/* 57 */     return evalFalse.eval(callstack, interpreter);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHTernaryExpression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */