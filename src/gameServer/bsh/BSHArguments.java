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
/*    */ class BSHArguments
/*    */   extends SimpleNode
/*    */ {
/*    */   BSHArguments(int id) {
/* 39 */     super(id);
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
/*    */   public Object[] getArguments(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 62 */     Object[] args = new Object[jjtGetNumChildren()];
/* 63 */     for (int i = 0; i < args.length; i++) {
/*    */       
/* 65 */       args[i] = ((SimpleNode)jjtGetChild(i)).eval(callstack, interpreter);
/* 66 */       if (args[i] == Primitive.VOID) {
/* 67 */         throw new EvalError("Undefined argument: " + ((SimpleNode)jjtGetChild(i)).getText(), this, callstack);
/*    */       }
/*    */     } 
/*    */     
/* 71 */     return args;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHArguments.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */