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
/*    */ class BSHWhileStatement
/*    */   extends SimpleNode
/*    */   implements ParserConstants
/*    */ {
/*    */   public boolean isDoStatement;
/*    */   
/*    */   BSHWhileStatement(int id) {
/* 44 */     super(id);
/*    */   }
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/*    */     SimpleNode condExp;
/* 49 */     int numChild = jjtGetNumChildren();
/*    */ 
/*    */     
/* 52 */     SimpleNode body = null;
/*    */     
/* 54 */     if (this.isDoStatement) {
/* 55 */       condExp = (SimpleNode)jjtGetChild(1);
/* 56 */       body = (SimpleNode)jjtGetChild(0);
/*    */     } else {
/* 58 */       condExp = (SimpleNode)jjtGetChild(0);
/* 59 */       if (numChild > 1) {
/* 60 */         body = (SimpleNode)jjtGetChild(1);
/*    */       }
/*    */     } 
/* 63 */     boolean doOnceFlag = this.isDoStatement;
/*    */     
/* 65 */     while (doOnceFlag || BSHIfStatement.evaluateCondition(condExp, callstack, interpreter)) {
/*    */ 
/*    */ 
/*    */       
/* 69 */       if (body == null) {
/*    */         continue;
/*    */       }
/* 72 */       Object ret = body.eval(callstack, interpreter);
/*    */       
/* 74 */       boolean breakout = false;
/* 75 */       if (ret instanceof ReturnControl)
/*    */       {
/* 77 */         switch (((ReturnControl)ret).kind) {
/*    */           
/*    */           case 46:
/* 80 */             return ret;
/*    */           
/*    */           case 19:
/*    */             continue;
/*    */           
/*    */           case 12:
/* 86 */             breakout = true;
/*    */             break;
/*    */         } 
/*    */       }
/* 90 */       if (breakout) {
/*    */         break;
/*    */       }
/* 93 */       doOnceFlag = false;
/*    */     } 
/*    */     
/* 96 */     return Primitive.VOID;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHWhileStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */