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
/*    */ class BSHIfStatement
/*    */   extends SimpleNode
/*    */ {
/*    */   BSHIfStatement(int id) {
/* 39 */     super(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 44 */     Object ret = null;
/*    */     
/* 46 */     if (evaluateCondition((SimpleNode)jjtGetChild(0), callstack, interpreter)) {
/*    */       
/* 48 */       ret = ((SimpleNode)jjtGetChild(1)).eval(callstack, interpreter);
/*    */     }
/* 50 */     else if (jjtGetNumChildren() > 2) {
/* 51 */       ret = ((SimpleNode)jjtGetChild(2)).eval(callstack, interpreter);
/*    */     } 
/* 53 */     if (ret instanceof ReturnControl) {
/* 54 */       return ret;
/*    */     }
/* 56 */     return Primitive.VOID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean evaluateCondition(SimpleNode condExp, CallStack callstack, Interpreter interpreter) throws EvalError {
/* 63 */     Object obj = condExp.eval(callstack, interpreter);
/* 64 */     if (obj instanceof Primitive) {
/* 65 */       if (obj == Primitive.VOID) {
/* 66 */         throw new EvalError("Condition evaluates to void type", condExp, callstack);
/*    */       }
/* 68 */       obj = ((Primitive)obj).getValue();
/*    */     } 
/*    */     
/* 71 */     if (obj instanceof Boolean) {
/* 72 */       return ((Boolean)obj).booleanValue();
/*    */     }
/* 74 */     throw new EvalError("Condition must evaluate to a Boolean or boolean.", condExp, callstack);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHIfStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */