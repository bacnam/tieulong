/*    */ package jsc.swt.plot;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.plaf.basic.BasicSliderUI;
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
/*    */ public class BeadSliderUI
/*    */   extends BasicSliderUI
/*    */ {
/* 25 */   Color colour = Color.black;
/* 26 */   Color focusColour = Color.black;
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
/*    */   public BeadSliderUI() {
/* 46 */     super(null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Color getFocusColor() {
/* 57 */     return this.focusColour;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void paintThumb(Graphics paramGraphics) {
/* 68 */     paramGraphics.setColor(this.colour);
/*    */     
/* 70 */     int i = this.thumbRect.y + this.thumbRect.height / 2;
/*    */     
/* 72 */     int j = this.contentRect.width;
/* 73 */     paramGraphics.fillOval(this.contentRect.x, i - j / 2, j, j);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBeadColour(Color paramColor) {
/* 82 */     this.colour = paramColor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFocusColour(Color paramColor) {
/* 93 */     this.focusColour = paramColor;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot/BeadSliderUI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */