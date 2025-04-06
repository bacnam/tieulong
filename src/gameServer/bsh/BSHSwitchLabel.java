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
/*    */ class BSHSwitchLabel
/*    */   extends SimpleNode
/*    */ {
/*    */   boolean isDefault;
/*    */   
/*    */   public BSHSwitchLabel(int id) {
/* 39 */     super(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 44 */     if (this.isDefault)
/* 45 */       return null; 
/* 46 */     SimpleNode label = (SimpleNode)jjtGetChild(0);
/* 47 */     return label.eval(callstack, interpreter);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHSwitchLabel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */