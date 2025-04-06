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
/*    */ class BSHStatementExpressionList
/*    */   extends SimpleNode
/*    */ {
/*    */   BSHStatementExpressionList(int id) {
/* 39 */     super(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 44 */     int n = jjtGetNumChildren();
/* 45 */     for (int i = 0; i < n; i++) {
/*    */       
/* 47 */       SimpleNode node = (SimpleNode)jjtGetChild(i);
/* 48 */       node.eval(callstack, interpreter);
/*    */     } 
/* 50 */     return Primitive.VOID;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHStatementExpressionList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */