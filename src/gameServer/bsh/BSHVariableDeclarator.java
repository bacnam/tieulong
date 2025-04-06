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
/*    */ class BSHVariableDeclarator
/*    */   extends SimpleNode
/*    */ {
/*    */   public String name;
/*    */   
/*    */   BSHVariableDeclarator(int id) {
/* 46 */     super(id);
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
/*    */   public Object eval(BSHType typeNode, CallStack callstack, Interpreter interpreter) throws EvalError {
/* 67 */     Object value = null;
/*    */     
/* 69 */     if (jjtGetNumChildren() > 0) {
/*    */       
/* 71 */       SimpleNode initializer = (SimpleNode)jjtGetChild(0);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 79 */       if (typeNode != null && initializer instanceof BSHArrayInitializer) {
/*    */ 
/*    */         
/* 82 */         value = ((BSHArrayInitializer)initializer).eval(typeNode.getBaseType(), typeNode.getArrayDims(), callstack, interpreter);
/*    */       }
/*    */       else {
/*    */         
/* 86 */         value = initializer.eval(callstack, interpreter);
/*    */       } 
/*    */     } 
/* 89 */     if (value == Primitive.VOID) {
/* 90 */       throw new EvalError("Void initializer.", this, callstack);
/*    */     }
/* 92 */     return value;
/*    */   }
/*    */   public String toString() {
/* 95 */     return "BSHVariableDeclarator " + this.name;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHVariableDeclarator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */