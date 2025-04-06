/*    */ package org.slf4j.helpers;
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
/*    */ public final class Util
/*    */ {
/*    */   private static final class ClassContextSecurityManager
/*    */     extends SecurityManager
/*    */   {
/*    */     private ClassContextSecurityManager() {}
/*    */     
/*    */     protected Class<?>[] getClassContext() {
/* 45 */       return super.getClassContext();
/*    */     }
/*    */   }
/*    */   
/* 49 */   private static final ClassContextSecurityManager SECURITY_MANAGER = new ClassContextSecurityManager();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Class<?> getCallingClass() {
/* 57 */     Class<?>[] trace = SECURITY_MANAGER.getClassContext();
/* 58 */     String thisClassName = Util.class.getName();
/*    */     
/*    */     int i;
/*    */     
/* 62 */     for (i = 0; i < trace.length && 
/* 63 */       !thisClassName.equals(trace[i].getName()); i++);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 68 */     if (i >= trace.length || i + 2 >= trace.length) {
/* 69 */       throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; this should not happen");
/*    */     }
/*    */     
/* 72 */     return trace[i + 2];
/*    */   }
/*    */   
/*    */   public static final void report(String msg, Throwable t) {
/* 76 */     System.err.println(msg);
/* 77 */     System.err.println("Reported exception:");
/* 78 */     t.printStackTrace();
/*    */   }
/*    */   
/*    */   public static final void report(String msg) {
/* 82 */     System.err.println("SLF4J: " + msg);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/slf4j/helpers/Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */