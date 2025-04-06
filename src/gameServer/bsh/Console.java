/*    */ package bsh;
/*    */ 
/*    */ import bsh.util.Util;
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
/*    */ public class Console
/*    */ {
/*    */   public static void main(String[] args) {
/* 46 */     if (!Capabilities.classExists("bsh.util.Util")) {
/* 47 */       System.out.println("Can't find the BeanShell utilities...");
/*    */     }
/* 49 */     if (Capabilities.haveSwing()) {
/*    */       
/* 51 */       Util.startSplashScreen();
/*    */       try {
/* 53 */         (new Interpreter()).eval("desktop()");
/* 54 */       } catch (EvalError e) {
/* 55 */         System.err.println("Couldn't start desktop: " + e);
/*    */       } 
/*    */     } else {
/* 58 */       System.err.println("Can't find javax.swing package:  An AWT based Console is available but not built by default.");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Console.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */