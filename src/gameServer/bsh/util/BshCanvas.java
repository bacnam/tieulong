/*    */ package bsh.util;
/*    */ 
/*    */ import bsh.EvalError;
/*    */ import bsh.Interpreter;
/*    */ import bsh.This;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import javax.swing.JComponent;
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
/*    */ public class BshCanvas
/*    */   extends JComponent
/*    */ {
/*    */   This ths;
/*    */   Image imageBuffer;
/*    */   
/*    */   public BshCanvas() {}
/*    */   
/*    */   public BshCanvas(This ths) {
/* 57 */     this.ths = ths;
/*    */   }
/*    */ 
/*    */   
/*    */   public void paintComponent(Graphics g) {
/* 62 */     if (this.imageBuffer != null) {
/* 63 */       g.drawImage(this.imageBuffer, 0, 0, this);
/*    */     }
/*    */     
/* 66 */     if (this.ths != null) {
/*    */       try {
/* 68 */         this.ths.invokeMethod("paint", new Object[] { g });
/* 69 */       } catch (EvalError e) {
/* 70 */         if (Interpreter.DEBUG) Interpreter.debug("BshCanvas: method invocation error:" + e);
/*    */       
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Graphics getBufferedGraphics() {
/* 80 */     Dimension dim = getSize();
/* 81 */     this.imageBuffer = createImage(dim.width, dim.height);
/* 82 */     return this.imageBuffer.getGraphics();
/*    */   }
/*    */   
/*    */   public void setBounds(int x, int y, int width, int height) {
/* 86 */     setPreferredSize(new Dimension(width, height));
/* 87 */     setMinimumSize(new Dimension(width, height));
/* 88 */     super.setBounds(x, y, width, height);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/BshCanvas.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */