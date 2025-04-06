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
/*    */ class BSHReturnStatement
/*    */   extends SimpleNode
/*    */   implements ParserConstants
/*    */ {
/*    */   public int kind;
/*    */   
/*    */   BSHReturnStatement(int id) {
/* 41 */     super(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*    */     Object value;
/* 47 */     if (jjtGetNumChildren() > 0) {
/* 48 */       value = ((SimpleNode)jjtGetChild(0)).eval(callstack, interpreter);
/*    */     } else {
/* 50 */       value = Primitive.VOID;
/*    */     } 
/* 52 */     return new ReturnControl(this.kind, value, this);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHReturnStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */