/*    */ package bsh;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ClassGenerator
/*    */ {
/*    */   private static ClassGenerator cg;
/*    */   
/*    */   public static ClassGenerator getClassGenerator() throws UtilEvalError {
/* 13 */     if (cg == null) {
/*    */       
/*    */       try {
/* 16 */         Class<?> clas = Class.forName("bsh.ClassGeneratorImpl");
/* 17 */         cg = (ClassGenerator)clas.newInstance();
/* 18 */       } catch (Exception e) {
/* 19 */         throw new Capabilities.Unavailable("ClassGenerator unavailable: " + e);
/*    */       } 
/*    */     }
/*    */     
/* 23 */     return cg;
/*    */   }
/*    */   
/*    */   public abstract Class generateClass(String paramString, Modifiers paramModifiers, Class[] paramArrayOfClass, Class paramClass, BSHBlock paramBSHBlock, boolean paramBoolean, CallStack paramCallStack, Interpreter paramInterpreter) throws EvalError;
/*    */   
/*    */   public abstract Object invokeSuperclassMethod(BshClassManager paramBshClassManager, Object paramObject, String paramString, Object[] paramArrayOfObject) throws UtilEvalError, ReflectError, InvocationTargetException;
/*    */   
/*    */   public abstract void setInstanceNameSpaceParent(Object paramObject, String paramString, NameSpace paramNameSpace);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/ClassGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */