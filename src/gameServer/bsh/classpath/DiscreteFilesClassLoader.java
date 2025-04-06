/*    */ package bsh.classpath;
/*    */ 
/*    */ import bsh.BshClassManager;
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DiscreteFilesClassLoader
/*    */   extends BshClassLoader
/*    */ {
/*    */   ClassSourceMap map;
/*    */   
/*    */   public static class ClassSourceMap
/*    */     extends HashMap
/*    */   {
/*    */     public void put(String name, BshClassPath.ClassSource source) {
/* 60 */       put((K)name, (V)source);
/*    */     }
/*    */     public BshClassPath.ClassSource get(String name) {
/* 63 */       return (BshClassPath.ClassSource)get(name);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DiscreteFilesClassLoader(BshClassManager classManager, ClassSourceMap map) {
/* 70 */     super(classManager);
/* 71 */     this.map = map;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class findClass(String name) throws ClassNotFoundException {
/* 79 */     BshClassPath.ClassSource source = this.map.get(name);
/*    */     
/* 81 */     if (source != null) {
/*    */       
/* 83 */       byte[] code = source.getCode(name);
/* 84 */       return defineClass(name, code, 0, code.length);
/*    */     } 
/*    */ 
/*    */     
/* 88 */     return super.findClass(name);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 92 */     return super.toString() + "for files: " + this.map;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/classpath/DiscreteFilesClassLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */