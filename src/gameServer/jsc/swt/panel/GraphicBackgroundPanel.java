/*    */ package jsc.swt.panel;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.LayoutManager;
/*    */ import javax.swing.ImageIcon;
/*    */ import jsc.Utilities;
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
/*    */ public class GraphicBackgroundPanel
/*    */   extends TransparentChildPanel
/*    */ {
/*    */   ImageIcon tile;
/*    */   
/*    */   public GraphicBackgroundPanel(LayoutManager paramLayoutManager, ImageIcon paramImageIcon) {
/* 31 */     super(paramLayoutManager);
/* 32 */     this.tile = paramImageIcon;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void paintComponent(Graphics paramGraphics) {
/* 42 */     super.paintComponent(paramGraphics);
/* 43 */     Utilities.tile(paramGraphics, this, this.tile);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/panel/GraphicBackgroundPanel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */