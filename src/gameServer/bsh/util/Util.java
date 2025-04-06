/*    */ package bsh.util;
/*    */ 
/*    */ import bsh.Interpreter;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Frame;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import java.awt.MediaTracker;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.Window;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Util
/*    */ {
/*    */   static Window splashScreen;
/*    */   
/*    */   public static void startSplashScreen() {
/* 66 */     int width = 275, height = 148;
/* 67 */     Window win = new Window(new Frame());
/* 68 */     win.pack();
/* 69 */     BshCanvas can = new BshCanvas();
/* 70 */     can.setSize(width, height);
/* 71 */     Toolkit tk = Toolkit.getDefaultToolkit();
/* 72 */     Dimension dim = tk.getScreenSize();
/* 73 */     win.setBounds(dim.width / 2 - width / 2, dim.height / 2 - height / 2, width, height);
/*    */     
/* 75 */     win.add("Center", can);
/* 76 */     Image img = tk.getImage(Interpreter.class.getResource("/bsh/util/lib/splash.gif"));
/*    */     
/* 78 */     MediaTracker mt = new MediaTracker(can);
/* 79 */     mt.addImage(img, 0); 
/* 80 */     try { mt.waitForAll(); } catch (Exception e) {}
/* 81 */     Graphics gr = can.getBufferedGraphics();
/* 82 */     gr.drawImage(img, 0, 0, can);
/* 83 */     win.setVisible(true);
/* 84 */     win.toFront();
/* 85 */     splashScreen = win;
/*    */   }
/*    */   
/*    */   public static void endSplashScreen() {
/* 89 */     if (splashScreen != null)
/* 90 */       splashScreen.dispose(); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */