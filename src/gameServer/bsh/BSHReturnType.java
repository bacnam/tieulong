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
/*    */ class BSHReturnType
/*    */   extends SimpleNode
/*    */ {
/*    */   public boolean isVoid;
/*    */   
/*    */   BSHReturnType(int id) {
/* 41 */     super(id);
/*    */   }
/*    */   BSHType getTypeNode() {
/* 44 */     return (BSHType)jjtGetChild(0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTypeDescriptor(CallStack callstack, Interpreter interpreter, String defaultPackage) {
/* 50 */     if (this.isVoid) {
/* 51 */       return "V";
/*    */     }
/* 53 */     return getTypeNode().getTypeDescriptor(callstack, interpreter, defaultPackage);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class evalReturnType(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 60 */     if (this.isVoid) {
/* 61 */       return void.class;
/*    */     }
/* 63 */     return getTypeNode().getType(callstack, interpreter);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHReturnType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */