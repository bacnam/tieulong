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
/*    */ class BSHCastExpression
/*    */   extends SimpleNode
/*    */ {
/*    */   public BSHCastExpression(int id) {
/* 46 */     super(id);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 54 */     NameSpace namespace = callstack.top();
/* 55 */     Class toType = ((BSHType)jjtGetChild(0)).getType(callstack, interpreter);
/*    */     
/* 57 */     SimpleNode expression = (SimpleNode)jjtGetChild(1);
/*    */ 
/*    */     
/* 60 */     Object fromValue = expression.eval(callstack, interpreter);
/* 61 */     Class<?> fromType = fromValue.getClass();
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 66 */       return Types.castObject(fromValue, toType, 0);
/* 67 */     } catch (UtilEvalError e) {
/* 68 */       throw e.toEvalError(this, callstack);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHCastExpression.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */