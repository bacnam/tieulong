/*    */ package bsh.util;
/*    */ 
/*    */ import bsh.ConsoleInterface;
/*    */ import bsh.Interpreter;
/*    */ import java.applet.Applet;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Component;
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
/*    */ public class AWTDemoApplet
/*    */   extends Applet
/*    */ {
/*    */   public void init() {
/* 29 */     setLayout(new BorderLayout());
/* 30 */     ConsoleInterface console = new AWTConsole();
/* 31 */     add("Center", (Component)console);
/* 32 */     Interpreter interpreter = new Interpreter(console);
/* 33 */     (new Thread((Runnable)interpreter)).start();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/AWTDemoApplet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */