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
/*    */ class BSHImportDeclaration
/*    */   extends SimpleNode
/*    */ {
/*    */   public boolean importPackage;
/*    */   public boolean staticImport;
/*    */   public boolean superImport;
/*    */   
/*    */   BSHImportDeclaration(int id) {
/* 43 */     super(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 48 */     NameSpace namespace = callstack.top();
/* 49 */     if (this.superImport) {
/*    */       try {
/* 51 */         namespace.doSuperImport();
/* 52 */       } catch (UtilEvalError e) {
/* 53 */         throw e.toEvalError(this, callstack);
/*    */       }
/*    */     
/*    */     }
/* 57 */     else if (this.staticImport) {
/*    */       
/* 59 */       if (this.importPackage) {
/*    */         
/* 61 */         Class clas = ((BSHAmbiguousName)jjtGetChild(0)).toClass(callstack, interpreter);
/*    */         
/* 63 */         namespace.importStatic(clas);
/*    */       } else {
/* 65 */         throw new EvalError("static field imports not supported yet", this, callstack);
/*    */       }
/*    */     
/*    */     } else {
/*    */       
/* 70 */       String name = ((BSHAmbiguousName)jjtGetChild(0)).text;
/* 71 */       if (this.importPackage) {
/* 72 */         namespace.importPackage(name);
/*    */       } else {
/* 74 */         namespace.importClass(name);
/*    */       } 
/*    */     } 
/*    */     
/* 78 */     return Primitive.VOID;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHImportDeclaration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */