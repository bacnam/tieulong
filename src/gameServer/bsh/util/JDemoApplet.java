/*    */ package bsh.util;
/*    */ 
/*    */ import bsh.EvalError;
/*    */ import bsh.Interpreter;
/*    */ import bsh.TargetError;
/*    */ import java.awt.BorderLayout;
/*    */ import javax.swing.JApplet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JDemoApplet
/*    */   extends JApplet
/*    */ {
/*    */   public void init() {
/* 29 */     String debug = getParameter("debug");
/* 30 */     if (debug != null && debug.equals("true")) {
/* 31 */       Interpreter.DEBUG = true;
/*    */     }
/* 33 */     String type = getParameter("type");
/* 34 */     if (type != null && type.equals("desktop")) {
/*    */       
/*    */       try {
/* 37 */         (new Interpreter()).eval("desktop()");
/* 38 */       } catch (TargetError te) {
/* 39 */         te.printStackTrace();
/* 40 */         System.out.println(te.getTarget());
/* 41 */         te.getTarget().printStackTrace();
/* 42 */       } catch (EvalError evalError) {
/* 43 */         System.out.println(evalError);
/* 44 */         evalError.printStackTrace();
/*    */       } 
/*    */     } else {
/*    */       
/* 48 */       getContentPane().setLayout(new BorderLayout());
/* 49 */       JConsole console = new JConsole();
/* 50 */       getContentPane().add("Center", console);
/* 51 */       Interpreter interpreter = new Interpreter(console);
/* 52 */       (new Thread((Runnable)interpreter)).start();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/JDemoApplet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */