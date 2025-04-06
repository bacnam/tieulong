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
/*    */ public class BSHPackageDeclaration
/*    */   extends SimpleNode
/*    */ {
/*    */   public BSHPackageDeclaration(int id) {
/* 41 */     super(id);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 47 */     BSHAmbiguousName name = (BSHAmbiguousName)jjtGetChild(0);
/* 48 */     NameSpace namespace = callstack.top();
/* 49 */     namespace.setPackage(name.text);
/*    */     
/* 51 */     namespace.importPackage(name.text);
/* 52 */     return Primitive.VOID;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHPackageDeclaration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */