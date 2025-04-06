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
/*    */ class BSHFormalParameter
/*    */   extends SimpleNode
/*    */ {
/* 42 */   public static final Class UNTYPED = null;
/*    */   public String name;
/*    */   public Class type;
/*    */   
/*    */   BSHFormalParameter(int id) {
/* 47 */     super(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTypeDescriptor(CallStack callstack, Interpreter interpreter, String defaultPackage) {
/* 52 */     if (jjtGetNumChildren() > 0) {
/* 53 */       return ((BSHType)jjtGetChild(0)).getTypeDescriptor(callstack, interpreter, defaultPackage);
/*    */     }
/*    */ 
/*    */     
/* 57 */     return "Ljava/lang/Object;";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 66 */     if (jjtGetNumChildren() > 0) {
/* 67 */       this.type = ((BSHType)jjtGetChild(0)).getType(callstack, interpreter);
/*    */     } else {
/* 69 */       this.type = UNTYPED;
/*    */     } 
/* 71 */     return this.type;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHFormalParameter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */