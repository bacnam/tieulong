/*    */ package jsc.swt.control;
/*    */ 
/*    */ import javax.swing.JSlider;
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
/*    */ public class IntegerSlider
/*    */   extends JSlider
/*    */ {
/*    */   public IntegerSlider(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/* 29 */     super(0, paramInt1, paramInt2, paramInt3);
/* 30 */     setMinorTickSpacing(paramInt4);
/* 31 */     setMajorTickSpacing(paramInt5);
/* 32 */     setPaintTicks(true);
/*    */     
/* 34 */     setPaintLabels(true);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/IntegerSlider.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */