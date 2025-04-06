/*    */ package bsh;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class BSHMethodInvocation
/*    */   extends SimpleNode
/*    */ {
/*    */   BSHMethodInvocation(int id) {
/* 41 */     super(id);
/*    */   }
/*    */   BSHAmbiguousName getNameNode() {
/* 44 */     return (BSHAmbiguousName)jjtGetChild(0);
/*    */   }
/*    */   
/*    */   BSHArguments getArgsNode() {
/* 48 */     return (BSHArguments)jjtGetChild(1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
/* 58 */     NameSpace namespace = callstack.top();
/* 59 */     BSHAmbiguousName nameNode = getNameNode();
/*    */ 
/*    */ 
/*    */     
/* 63 */     if (namespace.getParent() != null && (namespace.getParent()).isClass && (nameNode.text.equals("super") || nameNode.text.equals("this")))
/*    */     {
/*    */       
/* 66 */       return Primitive.VOID;
/*    */     }
/* 68 */     Name name = nameNode.getName(namespace);
/* 69 */     Object[] args = getArgsNode().getArguments(callstack, interpreter);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 75 */       return name.invokeMethod(interpreter, args, callstack, this);
/* 76 */     } catch (ReflectError e) {
/* 77 */       throw new EvalError("Error in method invocation: " + e.getMessage(), this, callstack);
/*    */     
/*    */     }
/* 80 */     catch (InvocationTargetException e) {
/*    */       
/* 82 */       String msg = "Method Invocation " + name;
/* 83 */       Throwable te = e.getTargetException();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 90 */       boolean isNative = true;
/* 91 */       if (te instanceof EvalError)
/* 92 */         if (te instanceof TargetError) {
/* 93 */           isNative = ((TargetError)te).inNativeCode();
/*    */         } else {
/* 95 */           isNative = false;
/*    */         }  
/* 97 */       throw new TargetError(msg, te, this, callstack, isNative);
/* 98 */     } catch (UtilEvalError e) {
/* 99 */       throw e.toEvalError(this, callstack);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/BSHMethodInvocation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */